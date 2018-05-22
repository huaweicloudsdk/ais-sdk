package com.huawei.ais.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huawei.ais.sdk.util.HttpClientUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * 使用Token认证方式访问服务
 */
public class LongSentenceTokenDemo {
	public static int connectionTimeout = 5000; //连接目标url超时限制参数
	public static int connectionRequestTimeout = 1000;//连接池获取可用连接超时限制参数
	public static int socketTimeout =  5000;//获取服务器响应数据超时限制参数

	private static final String ENDPOINT_HOST = "https://ais.cn-north-1.myhuaweicloud.com";
	private static final String URL_TEMPLATE = "/v1.0/voice/asr/long-sentence?job_id=%s";
	private static final long POLLING_INTERVAL = 2000L;

	/**
	 * 构造使用Token方式访问服务的请求Token对象
	 *
	 * @param username    用户名
	 * @param passwd      密码
	 * @param domainName  域名
	 * @param projectName 项目名称
	 * @return 构造访问的JSON对象
	 */
	private static String requestBody(String username, String passwd, String domainName, String projectName) {
		JSONObject auth = new JSONObject();

		JSONObject identity = new JSONObject();

		JSONArray methods = new JSONArray();
		methods.add("password");
		identity.put("methods", methods);

		JSONObject password = new JSONObject();

		JSONObject user = new JSONObject();
		user.put("name", username);
		user.put("password", passwd);

		JSONObject domain = new JSONObject();
		domain.put("name", domainName);
		user.put("domain", domain);

		password.put("user", user);

		identity.put("password", password);

		JSONObject scope = new JSONObject();

		JSONObject scopeProject = new JSONObject();
		scopeProject.put("name", projectName);

		scope.put("project", scopeProject);

		auth.put("identity", identity);
		auth.put("scope", scope);

		JSONObject params = new JSONObject();
		params.put("auth", auth);
		return params.toJSONString();
	}

	/**
	 * 获取Token参数， 注意，此函数的目的，主要为了从HTTP请求返回体中的Header中提取出Token
	 * 参数名为: X-Subject-Token
	 *
	 * @param username   用户名
	 * @param domainname 账户名
	 * @param password   密码
	 * @param regionName 区域名，可以参
	 * @return 包含Token串的返回体，
	 * @throws URISyntaxException
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 */
	private static String getToken(String username, String domainname, String password, String regionName)
			throws URISyntaxException, UnsupportedOperationException, IOException {
		String requestBody = requestBody(username, password, domainname, regionName);
		String url = "https://iam.cn-north-1.myhuaweicloud.com/v3/auth/tokens";

		Header[] headers = new Header[]{new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString())};
		StringEntity stringEntity = new StringEntity(requestBody,
				"utf-8");

		HttpResponse response = HttpClientUtils.post(url, headers, stringEntity, connectionTimeout, connectionRequestTimeout, socketTimeout);
		Header[] xst = response.getHeaders("X-Subject-Token");
		return xst[0].getValue();

	}

	/**
	 * 使用Base64编码后的文件方式，使用Token认证方式访问服务
	 *
	 * @param token    token认证串
	 * @param formFile 文件路径
	 * @throws IOException
	 */
	public static void requestSentenceBase64(String token, String formFile) throws IOException {

		// 2.构建访问长语音识别服务需要的参数
		String requestBody = generatePostBody(formFile);

		requestSentence(token, requestBody);
	}

	/**
	 * 使用url方式, Token认证方式访问服务
	 * @param token token认证串
	 * @param url 文件OBS服务上的路径, url方式
	 * @throws IOException
	 */
	public static void requestSentenceUrl(String token, String url) throws IOException {

		// 2.构建访问长语音识别服务需要的参数
		String requestBody = generatePostBodyByUrl(url);

		requestSentence(token, requestBody);
	}

	/**
	 *  长语音处理函数
	 * @param token token认证串
	 * @param requestBody 请求body
	 * @throws IOException
	 */
	private static void requestSentence(String token, String requestBody) throws IOException {
		try {
			String urlPost = ENDPOINT_HOST + "/v1.0/voice/asr/long-sentence";
			Header[] headers = new Header[]{new BasicHeader("X-Auth-Token", token),
					new BasicHeader("Content-Type", "application/json")};

			// 2.构建访问长语音识别服务需要的参数
			StringEntity stringEntity = new StringEntity(requestBody, "utf-8");

			// 3.传入长语音识别服务对应的uri参数, 传入长语音识别服务需要的参数，
			// 该参数主要通过JSON对象的方式传入, 使用POST方法调用服务
			HttpResponse response = HttpClientUtils.post(urlPost, headers, stringEntity, connectionTimeout, connectionRequestTimeout, socketTimeout);

			// 4.验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
			ResponseProcessUtils.processResponseStatus(response);

			if (response.getStatusLine().getStatusCode() != 200) {
				System.out.println("Long sentence process failed: submit the job failed!");
				return;
			}

			// 5.获取到提交成功的任务ID, 准备进行结果的查询
			String jobId = getJobId(response);

			// 5.1 构建进行查询的请求链接，并进行轮询查询，由于是异步任务，必须多次进行轮询
			// 直到结果状态为任务已处理完成
			String words = "";
			String urlGet = String.format(ENDPOINT_HOST + URL_TEMPLATE, jobId);
			System.out.println(urlGet);

			while (true) {

				// 5.1 发起请求
				HttpResponse getResponse = HttpClientUtils.get(urlGet, headers);
				String result = HttpClientUtils.convertStreamToString(getResponse.getEntity().getContent());
				int status = getProcessStatus(result);

				// 6.3 如果处理失败，直接退出
				if (status == -1) {
					System.out.println("Process the audio result failed!");
					break;
				}

				// 6.2 任务处理成功
				else if (status == 2) {
					// 7. 处理服务返回的字符流，输出识别结果。
					words = getProcessResult(result);
					break;
				}
				// status == 0 || status == 1
				else {
					// 6.1 如果没有返回，等待一段时间，继续进行轮询。
					Thread.sleep(POLLING_INTERVAL);
					continue;
				}
			}
			System.out.println(words);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将二进制文件转为经Base64编码之后的请求访问对象（JSON串格式）
	 *
	 * @param file 文件名
	 * @return 包含被Base64编码之后的文件字符流的JSON对象
	 * @throws IOException
	 */
	public static String generatePostBody(String file) throws IOException {
		byte[] fileData = FileUtils.readFileToByteArray(new File(file));
		String fileBase64Str = Base64.encodeBase64String(fileData);
		JSONObject json = new JSONObject();
		json.put("data", fileBase64Str);

		return json.toJSONString();
	}

	/**
	 * 生成基于url访问的请求体
	 *
	 * @param url 待处理的文件的obs路径
	 * @return 包含url的JSON对象
	 * @throws IOException
	 */
	public static String generatePostBodyByUrl(String url) throws IOException {
		JSONObject json = new JSONObject();
		json.put("url", url);

		return json.toJSONString();
	}

	//
	// 获取提交任务的任务ID
	//
	private static String getJobId(HttpResponse response) throws UnsupportedOperationException, IOException {
		String result = HttpClientUtils.convertStreamToString(response.getEntity().getContent());
		JSONObject resp = JSON.parseObject(result);
		JSONObject resultJson = (JSONObject) resp.get("result");
		String jobId = (String) resultJson.get("job_id");

		return jobId;
	}

	//
	//  获取长语音服务处理的结果状态
	//
	private static int getProcessStatus(String response) throws UnsupportedOperationException, IOException {
		JSONObject resp = JSON.parseObject(response);
		JSONObject result = (JSONObject) resp.get("result");
		int status = (int) result.get("status_code");

		return status;
	}

	//
	//  获取长语音服务识别的结果内容
	//
	private static String getProcessResult(String response) throws UnsupportedOperationException, IOException {
		JSONObject resp = JSON.parseObject(response);
		JSONObject result = (JSONObject) resp.get("result");
		String words = (String) result.get("words");

		return words;
	}

	/**
	 * 调用主入口函数
	 */
	public static void main(String[] args) throws URISyntaxException, UnsupportedOperationException, IOException {
		String username = "zhangshan";    // 此处，请输入用户名
		String domainname = "MyCompany";  // 此处，请输入账户名，参考地址：https://console.huaweicloud.com/iam/#/myCredential
		String password = "*******";	  // 此处，请输入对应用户名的密码
		String regionName = "cn-north-1"; // 此处，请输入服务的区域信息，参考地址: http://developer.huaweicloud.com/dev/endpoint

		// 1. 配置好访问语音识别服务的基本信息, 获取Token
		String token = getToken(username, domainname, password, regionName);
		// System.out.println(token);

		// 传文件的方式访问服务
		requestSentenceBase64(token, "data/sentence.wav");

		// url方式访问服务
		requestSentenceUrl(token, "https://ais-sample-data.obs.myhwclouds.com/lsr-1.mp3");

	}

}
