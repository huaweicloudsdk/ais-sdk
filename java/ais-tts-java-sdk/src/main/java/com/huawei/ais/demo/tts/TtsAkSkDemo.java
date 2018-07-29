package com.huawei.ais.demo.tts;

import com.alibaba.fastjson.JSONObject;
import com.huawei.ais.demo.ClientContextUtils;
import com.huawei.ais.demo.ResponseProcessUtils;
import com.huawei.ais.sdk.AisAccess;
import com.huawei.ais.sdk.AisAccessWithProxy;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

/**
 *  语音合成服务的使用示例类
 */
public class TtsAkSkDemo {
	//
	// 语音合成服务的使用示例函数
	//
	private static void ttsDemo() throws IOException {
		//
		// 1. 在ClientContextUtils类中, 配置好访问服务的基本信息,
		// 然后，在此处生成对应的一个客户端连接对象
		//
		// 设置三个超时参数限制连接超时，分别如下
		int connectionTimeout = 5000; //连接目标url超时限制
		int connectionRequestTimeout = 1000;//连接池获取可用连接超时限制
		int socketTimeout = 5000;//获取服务器响应数据超时限制

		AisAccess service = new AisAccess(ClientContextUtils.getAuthInfo(), connectionTimeout,connectionRequestTimeout,socketTimeout);

		//
		// 1.a 此处支持使用代理方式访问语音识别服务，用于不能直接访问华为云官网服务的情况, 例如，内网网络。
		// 如果使用此处方式，需要同时在ClientContextUtils中，配置相应的代理服务器的参数类(ProxyHostInfo)
		//
		//AisAccess service = new AisAccessWithProxy(ClientContextUtils.getAuthInfo(), ClientContextUtils.getProxyHost(), connectionTimeout, connectionRequestTimeout, socketTimeout);

		try {

			//
			// 2.构建访问语音合成服务需要的参数
			//
			String uri = "/v1.0/voice/tts";
			JSONObject json = new JSONObject();
			// 待合成文本
			json.put("text", "This is a test sample.");
			// 合成的声音人员标识。
			json.put("voice_name", "xiaoyu");
			// 语音的采样率，当前支持16k，代表16KHz
			json.put("sample_rate", "16k");
			// 语速: [-500, 500], 默认为0
			json.put("speech_speed", 0);
			// 音高：[-500, 500]，默认为0
			json.put("pitch_rate", 0);
			// 音量: [-20, 20], 默认为0
			json.put("volume", 0);
			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");

			// 3.传入语音合成服务对应的uri参数,
			// 该参数主要通过JSON对象的方式传入, 使用POST方法调用服务
			HttpResponse response = service.post(uri, stringEntity);

			// 4.验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败，成功时输出结果。
			if(ResponseProcessUtils.isOKResponded(response)) {
				String wavFilePath = "data/tts_token_sample.wav";
				ResponseProcessUtils.processResponseWithWavData(response, wavFilePath);
				System.out.println("voice file:" + wavFilePath);
			}
			else
			{
				ResponseProcessUtils.printResponse(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			// 5.使用完毕，关闭服务的客户端连接
			service.close();
		}
	}

	//
	// 主入口函数
	//
	public static void main(String[] args) throws IOException {

		// 测试入口函数
		ttsDemo();
	}
}
