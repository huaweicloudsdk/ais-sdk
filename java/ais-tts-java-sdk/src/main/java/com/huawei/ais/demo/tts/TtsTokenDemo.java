package com.huawei.ais.demo.tts;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huawei.ais.demo.ResponseProcessUtils;
import com.huawei.ais.sdk.util.HttpClientUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * 使用Token认证方式访问服务
 */
public class TtsTokenDemo {

	public static int connectionTimeout = 5000; //连接目标url超时限制参数
	public static int connectionRequestTimeout = 1000;//连接池获取可用连接超时限制参数
	public static int socketTimeout =  5000;//获取服务器响应数据超时限制参数

	/**
	 * 构造使用Token方式访问服务的请求Token对象
	 *
	 * @param username	用户名
	 * @param passwd	  密码
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
		StringEntity stringEntity = new StringEntity(requestBody, "utf-8");

		HttpResponse response = HttpClientUtils.post(url, headers, stringEntity, connectionTimeout, connectionRequestTimeout, socketTimeout);
		Header[] xst = response.getHeaders("X-Subject-Token");
		return xst[0].getValue();

	}

	/**
	 * 使用Token认证方式访问服务
	 *
	 * @param token	token认证串
	 * @param filePath 文件路径
	 * @throws IOException
	 */
	public static void requestTts(String token, String filePath) throws IOException {
		String url = "https://sis.cn-north-1.myhuaweicloud.com/v1.0/voice/tts";
		Header[] headers = new Header[]{new BasicHeader("X-Auth-Token", token), new BasicHeader("Content-Type", "application/json")};
		String requestBody = toTtsHttpBody();
		StringEntity stringEntity = new StringEntity(requestBody, "utf-8");
		try {
			HttpResponse response = HttpClientUtils.post(url, headers, stringEntity, connectionTimeout, connectionRequestTimeout, socketTimeout);

			//验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败，成功时输出结果。
			if(ResponseProcessUtils.isOKResponded(response)) {
				ResponseProcessUtils.processResponseWithWavData(response, filePath);
				System.out.println("voice file:" + filePath);
			}
			else
			{
				ResponseProcessUtils.printResponse(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成Tts需要数据的参数体
	 *
	 * @return 包含Tts需要数据的参数的JSON对象
	 * @throws IOException
	 */
	public static String toTtsHttpBody() throws IOException {
		JSONObject json = new JSONObject();
		// 待合成文本
		json.put("text", "This is a test sample.");
		// 合成的声音人员标识。
		json.put("voice_name", "xiaoyu");
		// 语音的采样率，当前支持16k，分别代表16KHz
		json.put("sample_rate", "16k");
		// 语速: [-500, 500], 默认为0
		json.put("speech_speed", "0");
		// 音高：[-500, 500]，默认为0
		json.put("pitch_rate", "0");
		// 音量: [-20, 20], 默认为0
		json.put("volume", "0");

		return json.toJSONString();
	}

	/**
	 * 调用主入口函数
	 */
	public static void main(String[] args) throws URISyntaxException, UnsupportedOperationException, IOException {
		String username = "username";	// 用户名
		String password = "******";	  // 密码
		String domainname = "domainname";  // 账户名，参考地址：https://console.huaweicloud.com/iam/#/myCredential
		String regionName = "cn-north-1"; // 此处，请输入服务的区域信息，参考地址: http://developer.huaweicloud.com/dev/endpoint
		String token = getToken(username, domainname, password, regionName);
		System.out.println("token:" + token);
		requestTts(token, "data/tts_token_sample.wav");
	}

}
