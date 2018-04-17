package com.huawei.ais.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huawei.ais.sdk.util.HttpClientUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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
public class TokenDemo {

	/**
	 * 构造使用Token方式访问服务的请求Token对象
	 * 
	 * @param username 用户名
	 * @param passwd 密码
	 * @param domainName 域名
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

		JSONObject projectDomain = new JSONObject();
		projectDomain.put("name", domainName);
		scopeProject.put("domain", projectDomain);

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
	 * @param password   密码
	 * @param projectName 区域名，可以参考http://developer.huaweicloud.com/dev/endpoint
	 * @return 包含Token串的返回体，
	 * @throws URISyntaxException
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 */
	private static String getToken(String username, String password, String projectName)
			throws URISyntaxException, UnsupportedOperationException, IOException {
		String requestBody = requestBody(username, password, username, projectName);
		String url ="https://iam.cn-north-1.myhuaweicloud.com/v3/auth/tokens"; 

		Header[] headers = new Header[] { new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()) };
		StringEntity stringEntity = new StringEntity(requestBody,
				"utf-8");

		HttpResponse response = HttpClientUtils.post(url, headers, stringEntity);
		Header[] xst = response.getHeaders("X-Subject-Token");
//		System.out.println(xst[0]);
		return xst[0].getValue();

	}
	
	/**
	 * 将二进制文件转为经Base64编码之后的请求访问对象（JSON串格式）
	 * 
	 * @param file 文件名
	 * @return 包含被Base64编码之后的文件字符流的JSON对象
	 * @throws IOException
	 */
	public static String toBase64Str(String file) throws IOException{
		byte[] fileData = FileUtils.readFileToByteArray(new File(file));
		String fileBase64Str = Base64.encodeBase64String(fileData);
		JSONObject json = new JSONObject();
		json.put("image", fileBase64Str);
		
		return json.toJSONString();
	}

	

	/**
	 * 图像反黄检测，使用Base64编码后的文件方式，使用Token认证方式访问服务
	 * @param token 
	 * 			token认证串
	 * @param formFile 
	 * 			文件路径
	 * @throws IOException
	 */
	public static void requestModerationAntiPornBase64(String token, String formFile) throws IOException {
		String url = "https://ais.cn-north-1.myhuaweicloud.com/v1.0/moderation/image/anti-porn";
		Header[] headers = new Header[] {new BasicHeader("X-Auth-Token", token) ,new BasicHeader("Content-Type", "application/json")};
		String requestBody=toBase64Str(formFile);
		StringEntity stringEntity = new StringEntity(requestBody, "utf-8");
		try {
			HttpResponse response = HttpClientUtils.post(url, headers, stringEntity);
			System.out.println(response);
			String content = IOUtils.toString(response.getEntity().getContent());
			System.out.println(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 清晰度检测，使用Base64编码后的文件方式，使用Token认证方式访问服务
	 * @param token 
	 * 			token认证串
	 * @param formFile 
	 * 			文件路径
	 * @throws IOException
	 */
	public static void requestModerationClarityBase64(String token, String formFile) throws IOException {
		String url = "https://ais.cn-north-1.myhuaweicloud.com/v1.0/moderation/image/clarity-detect";
		Header[] headers = new Header[] {new BasicHeader("X-Auth-Token", token) ,new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString())};
		try {
			byte[] fileData = FileUtils.readFileToByteArray(new File(formFile));
			String fileBase64Str = Base64.encodeBase64String(fileData);
			JSONObject json = new JSONObject();
			json.put("image", fileBase64Str);
			json.put("threshhold", 0.8);
						
			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");
			HttpResponse response = HttpClientUtils.post(url, headers, stringEntity);
			System.out.println(response);
			String content = IOUtils.toString(response.getEntity().getContent());
			System.out.println(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 扭曲矫正，使用Base64编码后的文件方式，使用Token认证方式访问服务
	 * @param token 
	 * 			token认证串
	 * @param formFile 
	 * 			文件路径
	 * @throws IOException
	 */
	public static void requestModerationDistortionCorrectBase64(String token, String formFile) throws IOException {
		String url = "https://ais.cn-north-1.myhuaweicloud.com/v1.0/moderation/image/distortion-correct";
		Header[] headers = new Header[] {new BasicHeader("X-Auth-Token", token) ,new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString())};
		try {
			byte[] fileData = FileUtils.readFileToByteArray(new File(formFile));
			String fileBase64Str = Base64.encodeBase64String(fileData);
			JSONObject json = new JSONObject();
			json.put("image", fileBase64Str);
			json.put("correction", true);
			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");
			
			HttpResponse response = HttpClientUtils.post(url, headers, stringEntity);
			
			if(ResponseProcessUtils.isRespondedOK(response)) {
				ResponseProcessUtils.processResponseWithImage(response, "data/moderation-distortion.corrected.jpg");
			} else {
				// 处理服务返回的字符流，输出识别结果。
				ResponseProcessUtils.processResponseStatus(response);
				String content = IOUtils.toString(response.getEntity().getContent());
				System.out.println(content);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 文本内容检测，使用Base64编码后的文件方式，使用Token认证方式访问服务
	 * @param token 
	 * 			token认证串
	 * @param formFile 
	 * 			文件路径
	 * @throws IOException
	 */
	public static void requestModerationTextContentBase64(String token, String textModeration) throws IOException {
		String url = "https://ais.cn-north-1.myhuaweicloud.com/v1.0/moderation/text";
		Header[] headers = new Header[] {new BasicHeader("X-Auth-Token", token) ,new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString())};
		try {
			JSONObject json = new JSONObject();
			json.put("categories", new String[] {"porn","politics"}); 
			
			JSONObject text = new JSONObject();
			text.put("text", textModeration);
			text.put("type", "content");
			
			JSONArray items = new JSONArray();
			items.add(text);
			
			json.put("items", items);
			
			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");
			HttpResponse response = HttpClientUtils.post(url, headers, stringEntity);
			System.out.println(response);
			String content = IOUtils.toString(response.getEntity().getContent());
			System.out.println(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 图像内容检测，使用Base64编码后的文件方式，使用Token认证方式访问服务
	 * @param token 
	 * 			token认证串
	 * @param formFile 
	 * 			文件路径
	 * @throws IOException
	 */
	public static void requestModerationImageContentBase64(String token, String formFile) throws IOException {
		String url = "https://ais.cn-north-1.myhuaweicloud.com/v1.0/moderation/image";
		Header[] headers = new Header[] {new BasicHeader("X-Auth-Token", token) ,new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString())};
		try {
			byte[] fileData = FileUtils.readFileToByteArray(new File(formFile));
			String fileBase64Str = Base64.encodeBase64String(fileData);
			JSONObject json = new JSONObject();
			json.put("image", fileBase64Str);
			json.put("correction", true);
			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");
			
			HttpResponse response = HttpClientUtils.post(url, headers, stringEntity);
			
			System.out.println(response);
			String content = IOUtils.toString(response.getEntity().getContent());
			System.out.println(content);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 调用主入口函数
	 */
	public static void main(String[] args) throws URISyntaxException, UnsupportedOperationException, IOException {
		String username = "***";    // 此处，请输入用户名
		String password = "***";	  // 此处，请输入对应用户名的密码
		String projectName = "cn-north-1"; // 此处，请输入服务的区域信息，参考地址: http://developer.huaweicloud.com/dev/endpoint
		String token = getToken(username, password, projectName);
		System.out.println(token);
		
		//运行图像反黄检测服务
	    //requestModerationAntiPornBase64(token, "data/moderation-demo-1.jpg");
		
		//运行清晰度检测服务
		//requestModerationClarityBase64(token, "data/moderation-demo-1.jpg");
		
		//运行扭曲矫正服务
		//requestModerationDistortionCorrectBase64(token, "data/moderation-demo-1.jpg");
		
		//运行文本内容检测服务
		//requestModerationTextContentBase64(token, "luo聊请+我，微信110");
		
		//运行图像内容检测服务
		requestModerationImageContentBase64(token, "data/moderation-demo-1.jpg");
				
	}

}
