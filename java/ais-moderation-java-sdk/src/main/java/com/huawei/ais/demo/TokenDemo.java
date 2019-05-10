package com.huawei.ais.demo;

import com.alibaba.fastjson.JSON;
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
	private static final String projectName = "cn-north-1"; // 此处，请输入服务的区域信息，目前支持华北-北京一(cn-north-1)以及亚太-香港(ap-southeast-1)
	private static final String URL_TEMPLATE = ServiceAccessBuilder.getCurrentEndpoint(projectName)+"/v1.0/moderation/image/batch?job_id=%s";
	private static final long POLLING_INTERVAL = 2000L;
	private static final Integer RETRY_MAX_TIMES = 3; // 查询任务失败的最大重试次数
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
		user.put("name", domainName);
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
		String url = ServiceAccessBuilder.getCurrentEndpoint(projectName)+"/v1.1/moderation/image/anti-porn";
		Header[] headers = new Header[] {new BasicHeader("X-Auth-Token", token) ,new BasicHeader("Content-Type", "application/json")};
		String requestBody=toBase64Str(formFile);
		StringEntity stringEntity = new StringEntity(requestBody, "utf-8");
		try {
			HttpResponse response = HttpClientUtils.post(url, headers, stringEntity, connectionTimeout, connectionRequestTimeout, socketTimeout);
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
		String url = ServiceAccessBuilder.getCurrentEndpoint(projectName)+"/v1.0/moderation/image/clarity-detect";
		Header[] headers = new Header[] {new BasicHeader("X-Auth-Token", token) ,new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString())};
		try {
			byte[] fileData = FileUtils.readFileToByteArray(new File(formFile));
			String fileBase64Str = Base64.encodeBase64String(fileData);
			JSONObject json = new JSONObject();
			json.put("image", fileBase64Str);
			json.put("threshhold", 0.8);
						
			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");
			HttpResponse response = HttpClientUtils.post(url, headers, stringEntity, connectionTimeout, connectionRequestTimeout, socketTimeout);
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
		String url = ServiceAccessBuilder.getCurrentEndpoint(projectName)+"/v1.0/moderation/image/distortion-correct";
		Header[] headers = new Header[] {new BasicHeader("X-Auth-Token", token) ,new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString())};
		try {
			byte[] fileData = FileUtils.readFileToByteArray(new File(formFile));
			String fileBase64Str = Base64.encodeBase64String(fileData);
			JSONObject json = new JSONObject();
			json.put("image", fileBase64Str);
			json.put("correction", true);
			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");
			
			HttpResponse response = HttpClientUtils.post(url, headers, stringEntity, connectionTimeout, connectionRequestTimeout, socketTimeout);
			
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
	 * @param textModeration
	 * 			文本内容
	 * @throws IOException
	 */
	public static void requestModerationTextContentBase64(String token, String textModeration) throws IOException {
		String url = ServiceAccessBuilder.getCurrentEndpoint(projectName)+"/v1.0/moderation/text";
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
			HttpResponse response = HttpClientUtils.post(url, headers, stringEntity, connectionTimeout, connectionRequestTimeout, socketTimeout);
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
		String url = ServiceAccessBuilder.getCurrentEndpoint(projectName)+"/v1.0/moderation/image";
		Header[] headers = new Header[] {new BasicHeader("X-Auth-Token", token) ,new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString())};
		try {
			byte[] fileData = FileUtils.readFileToByteArray(new File(formFile));
			String fileBase64Str = Base64.encodeBase64String(fileData);
			JSONObject json = new JSONObject();
			json.put("image", fileBase64Str);
			json.put("categories", new String[] {"politics", "ad"}); //检测内容
			json.put("threshold", 0);
			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");
			
			HttpResponse response = HttpClientUtils.post(url, headers, stringEntity, connectionTimeout, connectionRequestTimeout, socketTimeout);

			System.out.println(response);
			String content = IOUtils.toString(response.getEntity().getContent());
			System.out.println(content);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * 图像内容批量检测，使用obs对象方式，使用Token认证方式访问服务
     * @param token
     * 			token认证串
     * @param urls
     * 			obs 对象数组
     * @throws IOException
     */
    public static void requestModerationImageContentBatch(String token, String[] urls) throws IOException {
        String url = ServiceAccessBuilder.getCurrentEndpoint(projectName)+"/v1.0/moderation/image/batch";
        Header[] headers = new Header[] {new BasicHeader("X-Auth-Token", token) ,new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString())};
        try {
            JSONObject json = new JSONObject();
            json.put("urls", urls);
            json.put("categories", new String[] {"politics", "terrorism","porn","ad"}); //检测内容
            json.put("threshold", 0);
            StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");

            HttpResponse response = HttpClientUtils.post(url, headers, stringEntity, connectionTimeout, connectionRequestTimeout, socketTimeout);

            System.out.println(response);
            String content = IOUtils.toString(response.getEntity().getContent());
            System.out.println(content);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图像内容批量异步检测，使用obs对象方式，使用Token认证方式访问服务
     * @param token
     * 			token认证串
     * @param urls
     * 			obs 对象数组
     * @throws IOException
     */
	public static void requestModerationImageContentBatchJobs(String token, String[] urls){
		String url = ServiceAccessBuilder.getCurrentEndpoint(projectName)+"/v1.0/moderation/image/batch/jobs";
		Header[] headers = new Header[] {new BasicHeader("X-Auth-Token", token) ,new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString())};
		try {
			JSONObject json = new JSONObject();
			json.put("urls", urls);
			json.put("categories", new String[] {"politics"}); //检测内容
			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");

			HttpResponse response = HttpClientUtils.post(url, headers, stringEntity, connectionTimeout, connectionRequestTimeout, socketTimeout);

			// 验证服务调用返回的状态是否成功，如果为2xx, 为成功, 否则失败。
			ResponseProcessUtils.processResponseStatus(response);
			if(!HttpJsonDataUtils.isOKResponded(response)){
				System.out.println("Image content of batch jobs process is failed: submit the job failed!");
				return;
			}

			String jobId = getJobId(response);
			String uri = String.format(URL_TEMPLATE, jobId);
			System.out.println(uri);

			// 初始化查询jobId失败次数
			Integer retryTimes = 0;

			while(true){

				// 发起请求
				HttpResponse getResponse = HttpClientUtils.get(uri, headers);
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
				// 如果处理失败，直接退出
				if(status.equals("failed"))
				{
					System.out.println("Image content of batch jobs process result failed!");
					return;
				}

				// 任务处理成功
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
	//  获取图像内容检测处理的结果状态
	//
	private static String getProcessStatus(String response) throws UnsupportedOperationException, IOException
	{
		JSONObject resp = JSON.parseObject(response);
		JSONObject result = (JSONObject)resp.get("result");
		String status = result.get("status").toString();

		return status;
	}
	
	/**
	 * 调用主入口函数
	 */
	public static void main(String[] args) throws URISyntaxException, UnsupportedOperationException, IOException {
		String username = "zhangshan";		// 此处，请输入用户名
		String password = "*******";		// 此处，请输入对应用户名的密码

		String token = getToken(username, password, projectName);
		System.out.println(token);
		
		// 设置三个超时参数限制连接超时，分别如下
		connectionTimeout = 5000; //连接目标url超时限制
		connectionRequestTimeout = 1000;//连接池获取可用连接超时限制
		socketTimeout = 5000;//获取服务器响应数据超时限制
				
		//运行图像反黄检测服务
	    //requestModerationAntiPornBase64(token, "data/moderation-demo-1.jpg");
		
		//运行清晰度检测服务
		//requestModerationClarityBase64(token, "data/moderation-demo-1.jpg");
		
		//运行扭曲矫正服务
		//requestModerationDistortionCorrectBase64(token, "data/moderation-demo-1.jpg");
		
		//运行文本内容检测服务
		//requestModerationTextContentBase64(token, "luo聊请+我，微信110");
		
		//运行图像内容检测服务
		//requestModerationImageContentBase64(token, "data/moderation-demo-1.jpg");

		//运行图像内容检测异步批量服务
		String url1 = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg";
		String url2 = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg";
		requestModerationImageContentBatchJobs(token, new String[]{url1,url2});

		//运行图像内容检测批量服务
		//requestModerationImageContentBatch(token, new String[]{url1,url2});
				
	}

}
