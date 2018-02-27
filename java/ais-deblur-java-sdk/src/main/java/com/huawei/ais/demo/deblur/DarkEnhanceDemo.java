package com.huawei.ais.demo.deblur;

import java.io.File;
import java.io.IOException;

import com.huawei.ais.sdk.AisAccessWithProxy;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSONObject;
import com.huawei.ais.demo.ClientContextUtils;
import com.huawei.ais.demo.ResponseProcessUtils;
import com.huawei.ais.sdk.AisAccess;

/**
 *  低光照增强服务的使用示例类
 */
public class DarkEnhanceDemo {
	//
	// 低光照增强服务的使用示例函数
	//
	private static void darkEnhanceDemo() throws IOException {
		//
		// 1. 在ClientContextUtils类中, 配置好访问图像处理服务的基本信息,
		// 然后，在此处生成对应的一个客户端连接对象
		// 
		AisAccess service = new AisAccess(ClientContextUtils.getAuthInfo());
		
		//
		// 1.a 此处支持使用代理方式访问图像处理服务，用于不能直接访问华为云官网服务的情况, 例如，内网网络。
		// 如果使用此处方式，需要同时在ClientContextUtils中，配置相应的代理服务器的参数类(ProxyHostInfo)
		//
		//AisAccess service = new AisAccessWithProxy(ClientContextUtils.getAuthInfo(), ClientContextUtils.getProxyHost());
		
		try {
			
			//
			// 2.构建访问低光照增强服务需要的参数
			//
			String uri = "/v1.0/vision/dark-enhance";
			byte[] fileData = FileUtils.readFileToByteArray(new File("data/dark-enhance-demo-1.bmp"));
			String fileBase64Str = Base64.encodeBase64String(fileData);
	
			JSONObject json = new JSONObject();
			json.put("image", fileBase64Str);
			json.put("brightness", 0.9);

			// 3.传入低光照增强服务对应的uri参数, 传入低光照增强服务需要的参数，
			// 该参数主要通过JSON对象的方式传入, 使用POST方法调用服务
			HttpResponse response = service.post(uri, json.toJSONString());
			
			// 4.验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
			ResponseProcessUtils.processResponseStatus(response);
			
			// 5.处理服务返回的字符流，生成对应的低光照增强处理后对应的图片文件。
			ResponseProcessUtils.processResponseWithImage(response, "data/dark-enhance-demo-1.cooked.bmp");

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
		darkEnhanceDemo();
	}
}
