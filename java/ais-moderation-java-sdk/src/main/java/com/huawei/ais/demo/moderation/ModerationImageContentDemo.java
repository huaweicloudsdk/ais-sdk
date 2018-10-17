package com.huawei.ais.demo.moderation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huawei.ais.demo.ClientContextUtils;
import com.huawei.ais.demo.ResponseProcessUtils;
import com.huawei.ais.sdk.AisAccess;
import com.huawei.ais.sdk.AisAccessWithProxy;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *  图像内容检测服务的使用示例类
 */
public class ModerationImageContentDemo {
	
	//
	// 1. 在ClientContextUtils类中, 配置好访问图像内容检测服务的基本信息,
	// 然后，在此处生成对应的一个客户端连接对象
	// 设置三个超时参数限制连接超时，分别如下
	private int connectionTimeout = 5000; //连接目标url超时限制
	private int connectionRequestTimeout = 1000;//连接池获取可用连接超时限制
	private int socketTimeout = 5000;//获取服务器响应数据超时限制
		
	private AisAccess service;
	
	public ModerationImageContentDemo() {
		service = new AisAccess(ClientContextUtils.getAuthInfo(), connectionTimeout,connectionRequestTimeout,socketTimeout);
		//
		// 1.a 此处支持使用代理方式访问图像内容检测服务，用于不能直接访问华为云官网服务的情况, 例如，内网网络。
		// 如果使用此处方式，需要同时在ClientContextUtils中，配置相应的代理服务器的参数类(ProxyHostInfo)
		//
		//service = new AisAccessWithProxy(ClientContextUtils.getAuthInfo(), ClientContextUtils.getProxyHost(), connectionTimeout,connectionRequestTimeout, socketTimeout);

	}
	

	private void imageContentCheck(byte[] imagebytes) throws IOException {
		try {
			//
			// 2.构建访问图像内容检测服务需要的参数
			//
			String uri = "/v1.0/moderation/image";
			
			String fileBase64Str = Base64.encodeBase64String(imagebytes);
						
			JSONObject json = new JSONObject();
			
			json.put("image", fileBase64Str);
			
			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");

			// 3.传入图像内容检测服务对应的uri参数, 传入图像内容检测服务需要的参数，
			// 该参数主要通过JSON对象的方式传入, 使用POST方法调用服务
			HttpResponse response = service.post(uri, stringEntity);

			// 4.验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
			ResponseProcessUtils.processResponseStatus(response);

			// 5.处理服务返回的字符流，输出识别结果。
			ResponseProcessUtils.processResponse(response);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			// 6.使用完毕，关闭服务的客户端连接			
			service.close();
		}
	}
	

	private void imageAntiporn(byte[] imagebytes) throws IOException {
		try {
			//
			// 2.构建访问图像内容检测服务需要的参数
			//
			String uri = "/v1.1/moderation/image/anti-porn";
			
			String fileBase64Str = Base64.encodeBase64String(imagebytes);
						
			JSONObject json = new JSONObject();
			
			json.put("image", fileBase64Str);
			
			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");

			// 3.传入图像内容检测服务对应的uri参数, 传入图像内容检测服务需要的参数，
			// 该参数主要通过JSON对象的方式传入, 使用POST方法调用服务
			HttpResponse response = service.post(uri, stringEntity);

			// 4.验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
			ResponseProcessUtils.processResponseStatus(response);

			// 5.处理服务返回的字符流，输出识别结果。
			ResponseProcessUtils.processResponse(response);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			// 6.使用完毕，关闭服务的客户端连接			
			service.close();
		}
	}
	
	public byte[] downloadUrl(String url) throws MalformedURLException, IOException {
		InputStream in = new URL(url).openStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		byte[] buffer = new byte[128];
		int n = in.read(buffer);
		while (n != -1) {
			out.write(buffer, 0, n);
			n = in.read(buffer);
		}
		in.close();
		out.close();
		
		return out.toByteArray();
	}

	//
	// 主入口函数
	//
	public static void main(String[] args) throws IOException {
		ModerationImageContentDemo tool = new ModerationImageContentDemo();
		byte[] imageBytes = tool.downloadUrl("http://moderation-demo.ei.huaweicloud.com/theme/images/imagga/image_tagging_04.jpg");
		tool.imageContentCheck(imageBytes);
		tool.imageAntiporn(imageBytes);
		
		imageBytes = FileUtils.readFileToByteArray(new File("data/moderation-demo-1.jpg"));
		tool.imageContentCheck(imageBytes);
		tool.imageAntiporn(imageBytes);
	}
}
