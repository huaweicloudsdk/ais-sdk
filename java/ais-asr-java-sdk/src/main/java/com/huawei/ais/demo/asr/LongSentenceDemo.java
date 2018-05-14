package com.huawei.ais.demo.asr;

import java.io.File;
import java.io.IOException;

import com.huawei.ais.sdk.AisAccessWithProxy;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huawei.ais.demo.ClientContextUtils;
import com.huawei.ais.demo.ResponseProcessUtils;
import com.huawei.ais.sdk.AisAccess;
import com.huawei.ais.sdk.util.HttpClientUtils;

/**
 *  长语音识别服务的使用示例类
 */
public class LongSentenceDemo {
	private static final String URL_TEMPLATE = "/v1.0/voice/asr/long-sentence?job_id=%s";
	private static final long POLLING_INTERVAL = 2000L;

	//
	// 长语音识别服务的使用示例函数
	//
	private static void longSentenceDemo() throws IOException {
		//
		// 1. 在ClientContextUtils类中, 配置好访问语音识别服务的基本信息,
		// 然后，在此处生成对应的一个客户端连接对象
		//
		AisAccess service = new AisAccess(ClientContextUtils.getAuthInfo());

		//
		// 1.a 此处支持使用代理方式访问语音识别服务，用于不能直接访问华为云官网服务的情况, 例如，内网网络。
		// 如果使用此处方式，需要同时在ClientContextUtils中，配置相应的代理服务器的参数类(ProxyHostInfo)
		//
		//AisAccess service = new AisAccessWithProxy(ClientContextUtils.getAuthInfo(), ClientContextUtils.getProxyHost());

		try {

			//
			// 2.构建访问长语音识别服务需要的参数
			//
			String uri = "/v1.0/voice/asr/long-sentence";
			byte[] fileData = FileUtils.readFileToByteArray(new File("data/sentence.wav"));
			String fileBase64Str = Base64.encodeBase64String(fileData);

			JSONObject json = new JSONObject();
			json.put("data", fileBase64Str);

			// 3.传入长语音识别服务对应的uri参数, 传入长语音识别服务需要的参数，
			// 该参数主要通过JSON对象的方式传入, 使用POST方法调用服务
			HttpResponse response = service.post(uri, json.toJSONString());

			// 4.验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
			ResponseProcessUtils.processResponseStatus(response);
			
			if(response.getStatusLine().getStatusCode() != 200 ){
				System.out.println("Long sentence process failed: submit the job failed!");
				return;
			}
			
			String jobId = getJobId(response);

			// 
			// 等待一段时间  进行轮询, 
			//
			String words = "";
			String url = String.format(URL_TEMPLATE, jobId);
			System.out.println(url);
			
			while(true){
				HttpResponse getResponse = service.get(url);
				String result = HttpClientUtils.convertStreamToString(getResponse.getEntity().getContent());
				int status = getProcessStatus(result);
				
				if( status == -1 )
				{
					// 5.3 如果失败，直接退出
					System.out.println("Process the audio result failed!");
					break;
				}
				else if( status == 2)
				{
					// 5.2 处理服务返回的字符流，输出识别结果。
					words = getProcessResult(result);
					break;
				}
				// status == 0 || status == 1
				else
				{
					// 5.1 如果没有返回，继续进行轮询。
					Thread.sleep(POLLING_INTERVAL);
					continue;
				}
			}
			System.out.println(words);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 6.使用完毕，关闭服务的客户端连接
			service.close();
		}
	}

	private static String getJobId(HttpResponse response) throws UnsupportedOperationException, IOException
	{
		String result = HttpClientUtils.convertStreamToString(response.getEntity().getContent());
		JSONObject resp = JSON.parseObject(result);
		JSONObject resultJson = (JSONObject)resp.get("result");
		String jobId = (String)resultJson.get("job_id");
		
		return jobId;
	}
	
	private static int getProcessStatus(String response) throws UnsupportedOperationException, IOException
	{
		JSONObject resp = JSON.parseObject(response);
		JSONObject result = (JSONObject)resp.get("result");
		int status = (int)result.get("status_code");
		
		return status;
	}
	
	private static String getProcessResult(String response) throws UnsupportedOperationException, IOException
	{
		JSONObject resp = JSON.parseObject(response);
		JSONObject result = (JSONObject)resp.get("result");
		String words = (String)result.get("words");
		
		return words;
	}
	
	//
	// 主入口函数
	//
	public static void main(String[] args) throws IOException {

		// 测试入口函数
		longSentenceDemo();
	}
}
