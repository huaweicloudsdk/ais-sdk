package com.huawei.ais.demo.vcm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import com.huawei.ais.demo.HttpJsonDataUtils;
import com.huawei.ais.demo.vcm.model.Category;
import com.huawei.ais.demo.vcm.model.FrameResult;
import com.huawei.ais.demo.vcm.model.JobMetaInfo;
import com.huawei.ais.demo.vcm.model.JobResult;
import com.huawei.ais.demo.vcm.model.JobStatus;
import com.huawei.ais.demo.vcm.model.Suggestion;
import com.huawei.ais.sdk.util.HttpClientUtils;

/**
 * 视频审核API Token方式使用示例
 */
public class VCMTokenDemo {

	private static final String REGION = "cn-north-1";
	private static final String AIS_ENDPOINT = "https://ais." + REGION + ".myhuaweicloud.com";
	private static final String IAM_ENDPOINT = "https://iam." + REGION + ".myhuaweicloud.com";

	private static final String TOKEN_URL = IAM_ENDPOINT + "/v3/auth/tokens";
	private static final String SUBMIT_JOB_URL = AIS_ENDPOINT + "/v1.0/moderation/video";
	private static final String GET_JOB_RESULT_URL_TEMPLATE = AIS_ENDPOINT + "/v1.0/moderation/video?job_id=%s";
	private static final String GET_JOB_LIST_URL = AIS_ENDPOINT + "/v1.0/moderation/video/jobs";

	private static final String JSON_ROOT = "result";
	private static final long QUERY_JOB_RESULT_INTERVAL = 2000L;

	private static int connectionTimeout = 5000; //连接目标url超时限制参数
	private static int connectionRequestTimeout = 1000;//连接池获取可用连接超时限制参数
	private static int socketTimeout = 5000;//获取服务器响应数据超时限制参数

	public static void main(String[] args)
			throws UnsupportedOperationException, IOException, InterruptedException {
		// 1. 配置好访问语音识别服务的基本信息, 获取Token
		String username = "zhangshan";    // 用户名
		String domainName = "MyCompany";  // 账户名，参考地址：https://console.huaweicloud.com/iam/#/myCredential
		String password = "*******";      // 对应用户名的密码
		String regionName = "cn-north-1"; // 服务的区域信息，参考地址: http://developer.huaweicloud.com/dev/endpoint

		String token = getToken(username, domainName, password, regionName);

		// 2. 准备好视频文件的OBS地址，公共读状态，或者临时授权下载状态
		String videoUrl = "https://ais-sample-data.obs.cn-north-1.myhwclouds.com/news.mp4";

		// 3. 调用视频审核服务
		callVCMService(token, videoUrl);
	}

	private static void callVCMService(String token, String videoUrl) throws InterruptedException, IOException {

		JobMetaInfo jobMetaInfo = new JobMetaInfo();
		// 设置必选参数
		jobMetaInfo.setUrl(videoUrl);
		// 设置可选参数
		jobMetaInfo.setFrameInterval(5);
		jobMetaInfo.addCategory(Category.POLITICS);
		jobMetaInfo.addCategory(Category.TERRORISM);
		jobMetaInfo.addCategory(Category.PORN);

		Header[] headers = new Header[]{
				new BasicHeader("X-Auth-Token", token),
				new BasicHeader("Content-Type", "application/json")};

		HttpResponse response = HttpClientUtils.post(SUBMIT_JOB_URL, headers,
				HttpJsonDataUtils.ObjectToHttpEntity(jobMetaInfo), connectionTimeout, connectionRequestTimeout, socketTimeout);

		if (!HttpJsonDataUtils.isOKResponded(response)) {
			System.out.println("Submit the job failed!");
			System.out.println("Request body:" + HttpJsonDataUtils.ObjectToJsonString(jobMetaInfo));
			System.out.println(HttpJsonDataUtils.responseToString(response));
			return;
		}

		// 获取到提交成功的任务ID, 准备进行结果的查询
		JobResult submitResult = HttpJsonDataUtils.getResponseObject(response, JobResult.class, JSON_ROOT);
		String jobId = submitResult.getId();
		System.out.println("\nSubmit job successfully, job_id=" + jobId);


		// 构建进行查询的请求链接，并进行轮询查询，由于是异步任务，必须多次进行轮询
		// 直到结果状态为任务已处理结束
		String url = String.format(GET_JOB_RESULT_URL_TEMPLATE, jobId);
		while (true) {
			HttpResponse getResponse = HttpClientUtils.get(url, headers);
			if (!HttpJsonDataUtils.isOKResponded(getResponse)) {
				System.out.println("Get " + url);
				System.out.println(HttpJsonDataUtils.responseToString(getResponse));
				break;
			}
			JobResult jobResult
					= HttpJsonDataUtils.getResponseObject(getResponse, JobResult.class, JSON_ROOT);
			JobStatus jobStatus = jobResult.getStatus();

			// 根据任务状态觉得继续轮询或者打印结果
			if (jobStatus == JobStatus.CREATED || jobStatus == JobStatus.RUNNING) {
				//如果任务还未处理完，等待一段时间，继续进行轮询
				System.out.println("Job " + jobResult.getStatus() + ", waiting...");
				Thread.sleep(QUERY_JOB_RESULT_INTERVAL);
			} else if (jobStatus == JobStatus.FAILED) {
				// 如果处理失败，直接退出
				System.out.println("\nJob failed! \ncause:" + jobResult.getCause());
				break;
			} else if (jobStatus == JobStatus.FINISH) {
				// 任务处理成功，打印结果
				System.out.println("\nJob finished!");
				processJobFinishedResult(jobResult);
				break;
			} else {
				System.out.println("Should not be here!");
			}
		}
	}

	/**
	 * 获取Token参数， 注意，此函数的目的，主要为了从HTTP请求返回体中的Header中提取出Token
	 * 参数名为: X-Subject-Token
	 *
	 * @param username   用户名
	 * @param domainName 账户名
	 * @param password   密码
	 * @param regionName 区域名
	 * @return 包含Token串的返回体，
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 */
	private static String getToken(String username, String domainName, String password, String regionName)
			throws UnsupportedOperationException, IOException {
		String requestBody = getTokenReqBody(username, password, domainName, regionName);

		Header[] headers = new Header[]{new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString())};
		StringEntity stringEntity = new StringEntity(requestBody, "utf-8");

		HttpResponse response = HttpClientUtils.post(TOKEN_URL, headers, stringEntity, connectionTimeout,
				connectionRequestTimeout, socketTimeout);
		if (!HttpJsonDataUtils.isOKResponded(response)) {
			System.out.println("Request body:" + HttpJsonDataUtils.prettify(requestBody));
			System.out.println(HttpJsonDataUtils.responseToString(response));
			return null;
		}
		Header[] xst = response.getHeaders("X-Subject-Token");
		return xst[0].getValue();

	}

	private static void processJobFinishedResult(JobResult jobResult) {
		Suggestion generalSuggestion = jobResult.getSuggestion();
		System.out.println("\nSuggestion:" + generalSuggestion);

		if (generalSuggestion != Suggestion.PASS) {
			List<FrameResult> blockFrames = new ArrayList<>();
			List<FrameResult> reviewFrames = new ArrayList<>();
			jobResult.getFrameResults().forEach(frameResult -> {
				switch (frameResult.getFrameSuggestion()) {
					case PASS:
						//if pass no action needed
						break;
					case BLOCK:
						blockFrames.add(frameResult);
						break;
					case REVIEW:
						reviewFrames.add(frameResult);
						break;
				}
			});
			System.out.println("Detail：");
			if (!blockFrames.isEmpty()) {
				System.out.println("\tFrames blocked：");
				blockFrames.forEach(blockFrame -> System.out.println(String.format("\t\ttime:%02d~%02ds  categories:%s",
						blockFrame.getFrameBegin(), blockFrame.getFrameEnd(), blockFrame.getSuspectCategories())));
			}
			if (!reviewFrames.isEmpty()) {
				System.out.println("\tFrames need review：");
				reviewFrames.forEach(reviewFrame -> System.out.println(String.format("\t\ttime:%02d~%02ds  categories:%s",
						reviewFrame.getFrameBegin(), reviewFrame.getFrameEnd(), reviewFrame.getSuspectCategories())));
			}
		}
	}

	private static String getTokenReqBody(String username, String password, String domainName, String regionName) {
		return "{" +
				"    \"auth\":{" +
				"        \"identity\":{" +
				"            \"password\":{" +
				"                \"user\":{" +
				"                    \"name\":\"" + username + "\"," +
				"                    \"password\":\"" + password + "\"," +
				"                    \"domain\":{" +
				"                        \"name\":\"" + domainName + "\"" +
				"                    }" +
				"                }" +
				"            }," +
				"            \"methods\":[\"password\"]" +
				"        }," +
				"        \"scope\":{" +
				"            \"project\":{" +
				"                 \"name\":\"" + regionName + "\"" +
				"            }" +
				"        }" +
				"    }" +
				"}";
	}

}
