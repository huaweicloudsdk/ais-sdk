package com.huawei.ais.demo.moderation;

import java.io.File;
import java.io.IOException;

import com.huawei.ais.demo.HttpJsonDataUtils;
import com.huawei.ais.demo.ServiceAccessBuilder;
import com.huawei.ais.sdk.AisAccessWithProxy;
import com.huawei.ais.demo.obs.SimpleObsClient;
import com.huawei.ais.demo.obs.ObsFileHandle;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huawei.ais.demo.ResponseProcessUtils;
import com.huawei.ais.sdk.AisAccess;
import com.huawei.ais.sdk.util.HttpClientUtils;

/**
 *  图像内容批量异步检测服务的使用示例类
 */
public class ModerationImageContentBatchJobsDemo {
	private static final String URL_TEMPLATE = "/v1.0/moderation/image/batch?job_id=%s";
	private static final long POLLING_INTERVAL = 2000L;
	private static final Integer RETRY_MAX_TIMES = 3; // 查询任务失败的最大重试次数

	//
	// 图像内容批量异步服务的使用示例函数
	//
	private static void batchJobsDemo() throws IOException {

		// 1. 配置好访问图像内容批量异步服务的基本信息,生成对应的一个客户端连接对象
		AisAccess service = ServiceAccessBuilder.builder()
				.ak("######")                       // your ak
				.sk("######")                       // your sk
				.region("cn-north-1")               // 内容审核服务目前支持华北-北京一(cn-north-1)以及亚太-香港(ap-southeast-1)
				.connectionTimeout(5000)            // 连接目标url超时限制
				.connectionRequestTimeout(1000)     // 连接池获取可用连接超时限制
				.socketTimeout(20000)               // 获取服务器响应数据超时限制
				.build();

		try {

			//
			// 2.构建访问图像内容批量异步检测服务需要的参数
			//
			String uri = "/v1.0/moderation/image/batch/jobs";

			String url1 = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg";
			String url2 = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg";

			JSONObject json = new JSONObject();
			json.put("urls", new String[] {url1,url2}); 					//检测的obs对象数组，可自行添加
			json.put("categories", new String[] {"porn","politics"}); 		//检测场景

			// 3.传入图像内容异步批量检测服务对应的uri参数, 以及其他需要参数
			// 该参数主要通过JSON对象的方式传入, 使用POST方法调用服务
			HttpResponse response = service.post(uri, json.toJSONString());

			// 4.验证服务调用返回的状态是否成功，如果为2xx, 为成功, 否则失败。
			ResponseProcessUtils.processResponseStatus(response);
			
			if(!HttpJsonDataUtils.isOKResponded(response)){
				System.out.println("Image content of batch jobs process is failed: submit the job failed!");
				return;
			}

			// 5.获取到提交成功的任务ID, 准备进行结果的查询
			String jobId = getJobId(response);

			// 初始化查询jobId失败次数
			Integer retryTimes = 0;

			// 5.1 构建进行查询的请求链接，并进行轮询查询，由于是异步任务，必须多次进行轮询
			// 直到结果状态为任务已处理完成
			String url = String.format(URL_TEMPLATE, jobId);
			System.out.println(url);
			
			while(true){

				// 5.1 发起请求
				HttpResponse getResponse = service.get(url);

				// 5.2 验证服务调用返回的状态是否成功，如果为2xx, 为成功, 否则失败。
				if(!HttpJsonDataUtils.isOKResponded(getResponse)){
					System.out.println(HttpJsonDataUtils.responseToString(getResponse));
					if(retryTimes < RETRY_MAX_TIMES){
						retryTimes++;
						System.out.println(String.format("Image content of batch jobs process result failed! The number of retries is %s!", retryTimes));
						Thread.sleep(POLLING_INTERVAL);
						continue;
					}else{
						System.out.println("Image content of batch jobs process result failed! The number of retries has run out");
						return;
					}
				}

				String result = HttpClientUtils.convertStreamToString(getResponse.getEntity().getContent());
				String status = getProcessStatus(result);

				// 6.3 如果处理失败，直接退出
				if(status.equals("failed"))
				{
					System.out.println("Image content of batch jobs process result failed!");
					return;
				}

				// 6.2 任务处理成功
				else if( status.equals("finish"))
				{
					System.out.println(result);
					return;
				}
				// status == 0 || status == 1
				else
				{
					// 6.1 如果没有返回，等待一段时间，继续进行轮询。
					Thread.sleep(POLLING_INTERVAL);
					continue;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 7.使用完毕，关闭服务的客户端连接
			service.close();
		}
	}

	//
	// 获取提交任务的任务ID
	//
	private static String getJobId(HttpResponse response) throws UnsupportedOperationException, IOException
	{
		String result = HttpClientUtils.convertStreamToString(response.getEntity().getContent());
		JSONObject resp = JSON.parseObject(result);
		JSONObject resultJson = (JSONObject)resp.get("result");
		String jobId = (String)resultJson.get("job_id");
		
		return jobId;
	}

	//
	//  获取图像内容批量异步检测处理的结果状态
	//
	private static String getProcessStatus(String response) throws UnsupportedOperationException, IOException
	{
		JSONObject resp = JSON.parseObject(response);
		JSONObject result = (JSONObject)resp.get("result");
		String status = result.get("status").toString();
		
		return status;
	}
	
	//
	// 主入口函数
	//
	public static void main(String[] args) throws IOException {

		// 测试入口函数
		batchJobsDemo();
	}
}
