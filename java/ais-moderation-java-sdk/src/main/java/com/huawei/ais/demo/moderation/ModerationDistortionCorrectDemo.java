package com.huawei.ais.demo.moderation;

import com.alibaba.fastjson.JSONObject;
import com.huawei.ais.demo.ClientContextUtils;
import com.huawei.ais.demo.RequestSetup;
import com.huawei.ais.demo.ResponseProcessUtils;
import com.huawei.ais.sdk.AisAccess;
import com.huawei.ais.sdk.AisAccessWithProxy;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;

import java.io.File;
import java.io.IOException;

/**
 *  扭曲矫正服务的使用示例类
 */
public class ModerationDistortionCorrectDemo {
	//
	// 扭曲矫正服务的使用示例函数
	//
	private static void moderationDistortionCorrectDemo() throws IOException {
		//
		// 1. 在ClientContextUtils类中, 配置好访问扭曲矫正服务的基本信息,
		// 然后，在此处生成对应的一个客户端连接对象
		// 设置三个超时参数限制连接超时，分别如下
		RequestSetup requestSetup = new RequestSetup(5000, 1000, 5000);

		AisAccess service = new AisAccess(ClientContextUtils.getAuthInfo(), requestSetup.getConnectionTimeout(),
				requestSetup.getConnectionRequestTimeout(), requestSetup.getSocketTimeout());

		//
		// 1.a 此处支持使用代理方式访问扭曲矫正服务，用于不能直接访问华为云官网服务的情况, 例如，内网网络。
		// 如果使用此处方式，需要同时在ClientContextUtils中，配置相应的代理服务器的参数类(ProxyHostInfo)
		//
		//AisAccess service = new AisAccessWithProxy(ClientContextUtils.getAuthInfo(), ClientContextUtils.getProxyHost(), connectionTimeout,connectionRequestTimeout, socketTimeout);

		try {
			//
			// 2.构建访问扭曲矫正服务需要的参数
			//
			String uri = "/v1.0/moderation/image/distortion-correct";
			byte[] fileData = FileUtils.readFileToByteArray(new File("data/moderation-demo-1.jpg"));
			String fileBase64Str = Base64.encodeBase64String(fileData);

			JSONObject json = new JSONObject();
			json.put("image", fileBase64Str); //检测图片
			json.put("correction", true); //判断是否进行扭曲矫正
			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");

			// 3.传入扭曲矫正服务对应的uri参数, 传入扭曲矫正服务需要的参数，
			// 该参数主要通过JSON对象的方式传入, 使用POST方法调用服务
			HttpResponse response = service.post(uri, stringEntity);

			// 4.验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
			if(ResponseProcessUtils.isRespondedOK(response)) {
				ResponseProcessUtils.processResponseWithImage(response, "data/moderation-distortion.corrected.jpg");
			} else {
				// 5.处理服务返回的字符流，输出识别结果。
				ResponseProcessUtils.processResponseStatus(response);
				ResponseProcessUtils.processResponse(response);
			}
						
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			// 6.使用完毕，关闭服务的客户端连接			
			service.close();
		}
	}

	//
	// 主入口函数
	//
	public static void main(String[] args) throws IOException {

		// 测试入口函数
		moderationDistortionCorrectDemo();
	}
}
