package com.huawei.ais.demo;

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
public class DeblurTokenDemo {
	private static final String projectName = "cn-north-1"; // 此处，请输入服务的区域信息，目前支持华北-北京一(cn-north-1)以及亚太-香港(ap-southeast-1)
	public static int connectionTimeout = 5000; //连接目标url超时限制参数
	public static int connectionRequestTimeout = 1000;//连接池获取可用连接超时限制参数
	public static int socketTimeout =  5000;//获取服务器响应数据超时限制参数

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

		HttpResponse response = HttpClientUtils.post(url, headers, stringEntity, connectionTimeout, connectionRequestTimeout, socketTimeout);
		Header[] xst = response.getHeaders("X-Subject-Token");
		return xst[0].getValue();

	}

	/**
	 * 使用Base64编码后的文件方式，使用Token认证方式访问服务
	 * @param token token认证串
	 * @param formFile 文件路径
	 * @throws IOException
	 */
	public static void requestDarkEnhanceBase64(String token, String formFile) throws IOException {
		String url = ServiceAccessBuilder.getCurrentEndpoint(projectName)+"/v1.0/vision/dark-enhance";
		Header[] headers = new Header[] {new BasicHeader("X-Auth-Token", token) ,new BasicHeader("Content-Type", "application/json")};
		String requestBody=toBase64Str(formFile);
		StringEntity stringEntity = new StringEntity(requestBody, "utf-8");
		try {
			HttpResponse response = HttpClientUtils.post(url, headers, stringEntity, connectionTimeout, connectionRequestTimeout, socketTimeout);

            //验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
            ResponseProcessUtils.processResponseStatus(response);

            //处理服务返回的字符流，生成对应的低光照增强处理后对应的图片文件。
            ResponseProcessUtils.processResponseWithImage(response, "data/dark-enhance-demo-1.cooked.bmp");
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
	public static String toBase64Str(String file) throws IOException{
		byte[] fileData = FileUtils.readFileToByteArray(new File(file));
		String fileBase64Str = Base64.encodeBase64String(fileData);
		JSONObject json = new JSONObject();
		json.put("image", fileBase64Str);
		json.put("brightness", 0.9);
		
		return json.toJSONString();
	}

	/**
	 * 调用主入口函数
	 */
	public static void main(String[] args) throws URISyntaxException, UnsupportedOperationException, IOException {
		String username = "zhangshan";    // 此处，请输入用户名
		String password = "*******";	  // 此处，请输入对应用户名的密码
		String token = getToken(username, password, projectName);
		System.out.println(token);
		requestDarkEnhanceBase64(token, "data/dark-enhance-demo-1.bmp");

	}

}
