package com.huawei.ais.demo;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicHeader;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huawei.ais.sdk.util.HttpClientUtils;

/**
 * 使用Token认证方式访问服务, 具体可参考如下文档说明：
 * http://support.huaweicloud.com/api-ocr/ocr_03_0006.html
 */
public class TokenDemo {

	/**
	 * 构造使用Token方式访问服务的请求Token对象
	 * 可参考文档如下进行配置:
	 * http://support.huaweicloud.com/api-ocr/ocr_03_0006.html
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
		domain.put("name", username);
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
	 * @param regionName 区域名，可以参
	 * @return 包含Token串的返回体，
	 * @throws URISyntaxException
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 */
	private static String getToken(String username, String password, String regionName)
			throws URISyntaxException, UnsupportedOperationException, IOException {

	    // 1.构建获取Token所需要的参数
	    String requestBody = requestBody(username, password, username, regionName);
		String url ="https://iam.cn-north-1.myhuaweicloud.com/v3/auth/tokens";
		Header[] headers = new Header[] { new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()) };
		StringEntity stringEntity = new StringEntity(requestBody,
				"utf-8");

        // 2.传入IAM服务对应的参数, 使用POST方法调用服务并解析出Token value
		HttpResponse response = HttpClientUtils.post(url, headers, stringEntity);
		Header[] xst = response.getHeaders("X-Subject-Token");
		return xst[0].getValue();

	}

    /**
     * 英文海关识别，使用Base64编码后的文件方式，使用Token认证方式访问服务
     * @param token token认证串
     * @param formFile 文件路径
     * @throws IOException
     */
    public static void requestOcrCustomsFormEnBase64(String token, String formFile) {

        // 1.构建英文海关单据识别服务所需要的参数
		String url = "https://ais.cn-north-1.myhuaweicloud.com/v1.0/ocr/action/ocr_form";
		Header[] headers = new Header[] {new BasicHeader("X-Auth-Token", token) };
		try {
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
			multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			FileBody fileBody = new FileBody(new File(formFile), ContentType.create("image/jpeg", "utf-8"));
			multipartEntityBuilder.addPart("file", fileBody);

            // 2.传入英文海关单据识别服务对应的参数, 使用POST方法调用服务并解析输出识别结果
			HttpResponse response = HttpClientUtils.post(url, headers, multipartEntityBuilder.build());
			System.out.println(response);
			String content = IOUtils.toString(response.getEntity().getContent());
			System.out.println(content);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

    /**
     * 增值税发票识别，使用Base64编码后的文件方式，使用Token认证方式访问服务
     * @param token token认证串
     * @param formFile 文件路径
     * @throws IOException
     */
    public static void requestOcrVatInvoiceBase64(String token, String formFile) {

        // 1.构建增值税发票识别服务所需要的参数
		String url = "https://ais.cn-north-1.myhuaweicloud.com/v1.0/ocr/vat-invoice";
		Header[] headers = new Header[] {new BasicHeader("X-Auth-Token", token), new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()) };
		try {
			byte[] fileData = FileUtils.readFileToByteArray(new File(formFile));
			String fileBase64Str = Base64.encodeBase64String(fileData);
			JSONObject json = new JSONObject();
			json.put("image", fileBase64Str);
			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");

			// 2.传入增值税发票识别服务对应的参数, 使用POST方法调用服务并解析输出识别结果
			HttpResponse response = HttpClientUtils.post(url, headers, stringEntity);
			System.out.println(response);
			String content = IOUtils.toString(response.getEntity().getContent());
			System.out.println(content);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

    /**
     * 身份证识别，使用Base64编码后的文件方式，使用Token认证方式访问服务
     * @param token token认证串
     * @param formFile 文件路径
     * @throws IOException
     */
    public static void requestOcrIDCardBase64(String token, String formFile) {

        // 1.构建身份证识别服务所需要的参数
		String url = "https://ais.cn-north-1.myhuaweicloud.com/v1.0/ocr/id-card";
		Header[] headers = new Header[] {new BasicHeader("X-Auth-Token", token), new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()) };
		try {
			byte[] fileData = FileUtils.readFileToByteArray(new File(formFile));
			String fileBase64Str = Base64.encodeBase64String(fileData);
			JSONObject json = new JSONObject();
			json.put("image", fileBase64Str);

			//
			// 此参数可选，指定获取身份证的正面或反面信息
			// "side"可选"front", "back"或""，不填默认为""，由算法自由判断，建议填写提高准确率
			//
			json.put("side","front");

			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");

			// 2.传入身份证识别服务对应的参数, 使用POST方法调用服务并解析输出识别结果
			HttpResponse response = HttpClientUtils.post(url, headers, stringEntity);
			System.out.println(response);
			String content = IOUtils.toString(response.getEntity().getContent());
			System.out.println(content);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 驾驶证识别，使用Base64编码后的文件方式，使用Token认证方式访问服务
	 * @param token token认证串
	 * @param formFile 文件路径
	 * @throws IOException
	 */
	public static void requestOcrDriverLicenseBase64(String token, String formFile) {

		// 1.构建驾驶证识别服务所需要的参数
		String url = "https://ais.cn-north-1.myhuaweicloud.com/v1.0/ocr/driver-license";
		Header[] headers = new Header[] {new BasicHeader("X-Auth-Token", token), new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()) };
		try {
			byte[] fileData = FileUtils.readFileToByteArray(new File(formFile));
			String fileBase64Str = Base64.encodeBase64String(fileData);
			JSONObject json = new JSONObject();
			json.put("image", fileBase64Str);
			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");

			// 2.传入驾驶证识别服务对应的参数, 使用POST方法调用服务并解析输出识别结果
			HttpResponse response = HttpClientUtils.post(url, headers, stringEntity);
			System.out.println(response);
			String content = IOUtils.toString(response.getEntity().getContent());
			System.out.println(content);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 行驶证识别，使用Base64编码后的文件方式，使用Token认证方式访问服务
	 * @param token token认证串
	 * @param formFile 文件路径
	 * @throws IOException
	 */
	public static void requestOcrVehicleLicenseBase64(String token, String formFile) {

		// 1.构建行驶证识别服务所需要的参数
		String url = "https://ais.cn-north-1.myhuaweicloud.com/v1.0/ocr/vehicle-license";
		Header[] headers = new Header[] {new BasicHeader("X-Auth-Token", token), new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()) };
		try {
			byte[] fileData = FileUtils.readFileToByteArray(new File(formFile));
			String fileBase64Str = Base64.encodeBase64String(fileData);
			JSONObject json = new JSONObject();
			json.put("image", fileBase64Str);
			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");

			// 2.传入行驶证识别服务对应的参数, 使用POST方法调用服务并解析输出识别结果
			HttpResponse response = HttpClientUtils.post(url, headers, stringEntity);
			System.out.println(response);
			String content = IOUtils.toString(response.getEntity().getContent());
			System.out.println(content);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 通用表格，使用Base64编码后的文件方式，使用Token认证方式访问服务
	 * @param token token认证串
	 * @param formFile 文件路径
	 * @throws IOException
	 */
	public static void requestOcrGeneralTableBase64(String token, String formFile) {

		// 1.构建通用表格识别服务所需要的参数
		String url = "https://ais.cn-north-1.myhuaweicloud.com/v1.0/ocr/general-table";
		Header[] headers = new Header[] {new BasicHeader("X-Auth-Token", token), new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()) };
		try {
			byte[] fileData = FileUtils.readFileToByteArray(new File(formFile));
			String fileBase64Str = Base64.encodeBase64String(fileData);
			JSONObject json = new JSONObject();
			json.put("image", fileBase64Str);
			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");

			// 2.传入通用表格识别服务对应的参数, 使用POST方法调用服务并解析输出识别结果
			HttpResponse response = HttpClientUtils.post(url, headers, stringEntity);
			System.out.println(response);
			String content = IOUtils.toString(response.getEntity().getContent());
			System.out.println(content);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 手写体识别，使用Base64编码后的文件方式，使用Token认证方式访问服务
	 * @param token token认证串
	 * @param formFile 文件路径
	 * @throws IOException
	 */
	public static void requestOcrHandwritingBase64(String token, String formFile) {

		// 1.构建手写体识别服务所需要的参数
		String url = "https://ais.cn-north-1.myhuaweicloud.com/v1.0/ocr/handwriting";
		Header[] headers = new Header[] {new BasicHeader("X-Auth-Token", token), new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()) };
		try {
			byte[] fileData = FileUtils.readFileToByteArray(new File(formFile));
			String fileBase64Str = Base64.encodeBase64String(fileData);
			JSONObject json = new JSONObject();
			json.put("image", fileBase64Str);

			// 
			// 1.a 此项参数可选，可以指定是否检测文字方向，不填则默认不检测文字方向，
			// detect_direction可选:true, false
			//
			json.put("detect_direction", true);

			// 
			// 1.b 此项参数可选，可以指定获取文字类型，目前仅支持大写字母以及数字类型， 不填则默认同时识别数字和大写字母类型的文字
			// text_type可选:"digit", "upper_letter"， 组合成列表
			//
			ArrayList<String> textType =  new ArrayList<String>();
			textType.add("digit");
			textType.add("upper_letter");
			json.put("text_type", textType);

			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");

			// 2.传入手写体识别服务对应的参数, 使用POST方法调用服务并解析输出识别结果
			HttpResponse response = HttpClientUtils.post(url, headers, stringEntity);
			System.out.println(response);
			String content = IOUtils.toString(response.getEntity().getContent());
			System.out.println(content);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
    /**
     * 银行卡识别，使用Base64编码后的文件方式，使用Token认证方式访问服务
     * @param token token认证串
     * @param formFile 文件路径
     * @throws IOException
     */
    public static void requestOcrBankCardBase64(String token, String formFile) {

        // 1.构建身份证识别服务所需要的参数
        String url = "https://ais.cn-north-1.myhuaweicloud.com/v1.0/ocr/bank-card";
        Header[] headers = new Header[] {new BasicHeader("X-Auth-Token", token), new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()) };
        try {
            byte[] fileData = FileUtils.readFileToByteArray(new File(formFile));
            String fileBase64Str = Base64.encodeBase64String(fileData);
            
            JSONObject json = new JSONObject();
            json.put("image", fileBase64Str);
            
            //图片的URL路径, 与image参数是二选一关系，目前仅支持华为云上OBS提供的临时授权或者匿名公开授权访问的URL。
            //json.put("url", "http://obs.myhuaweicloud.com/ObjectKey?AWSAccessKeyId=AccessKeyID"
            //       + "&Expires=ExpiresValue&Signature=signature");
            
            //
            // 1.a 此项参数可选，是否支持返回发卡机构信息, 默认不返回。
            // true: 支持。false: 不支持，默认不支持。
            //
            //json.put("issue", true);
            
            StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");
            
            // 3.传入银行卡识别服务对应的uri参数, 传入银行卡识别服务需要的参数，
            // 该参数主要通过JSON对象的方式传入, 使用POST方法调用服务
            HttpResponse response = HttpClientUtils.post(url, headers, stringEntity);
            
            // 4.验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
            ResponseProcessUtils.processResponseStatus(response);
            
            // 5.处理服务返回的字符流。
            ResponseProcessUtils.processResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        } 

    }

	/**
	 * 机动车购车发票识别，使用Base64编码后的文件方式，使用Token认证方式访问服务
	 * @param token token认证串
	 * @param formFile 文件路径
	 * @throws IOException
	 */
	public static void requestOcrMvsInvoiceBase64(String token, String formFile) {

		// 1.构建机动车购车发票识别服务所需要的参数
		String url = "https://ais.cn-north-1.myhuaweicloud.com/v1.0/ocr/mvs-invoice";
		Header[] headers = new Header[] {new BasicHeader("X-Auth-Token", token), new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()) };
		try {
			byte[] fileData = FileUtils.readFileToByteArray(new File(formFile));
			String fileBase64Str = Base64.encodeBase64String(fileData);
			JSONObject json = new JSONObject();
			json.put("image", fileBase64Str);
			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");

			// 2.传入机动车购车发票识别服务对应的参数, 使用POST方法调用服务并解析输出识别结果
			HttpResponse response = HttpClientUtils.post(url, headers, stringEntity);
			System.out.println(response);
			String content = IOUtils.toString(response.getEntity().getContent());
			System.out.println(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

    /**
	 * 一维码识别，使用Base64编码后的文件方式，使用Token认证方式访问服务
	 * @param token token认证串
	 * @param formFile 文件路径
	 * @throws IOException
	 */
	public static void requestOcrBarcodeBase64(String token, String formFile) {

		// 1.构建一维码识别服务所需要的参数
		String url = "https://ais.cn-north-1.myhuaweicloud.com/v1.0/ocr/barcode";
		Header[] headers = new Header[] {new BasicHeader("X-Auth-Token", token), new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()) };
		try {
			byte[] fileData = FileUtils.readFileToByteArray(new File(formFile));
			String fileBase64Str = Base64.encodeBase64String(fileData);
			JSONObject json = new JSONObject();
			json.put("image", fileBase64Str);
			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");

			// 2.传入一维码识别服务对应的参数, 使用POST方法调用服务并解析输出识别结果
			HttpResponse response = HttpClientUtils.post(url, headers, stringEntity);
			System.out.println(response);
			String content = IOUtils.toString(response.getEntity().getContent());
			System.out.println(content);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 二维码识别，使用Base64编码后的文件方式，使用Token认证方式访问服务
	 * @param token token认证串
	 * @param formFile 文件路径
	 * @throws IOException
	 */
	public static void requestOcrQRCodeBase64(String token, String formFile) {

		// 1.构建二维码识别服务所需要的参数
		String url = "https://ais.cn-north-1.myhuaweicloud.com/v1.0/ocr/qr-code";
		Header[] headers = new Header[] {new BasicHeader("X-Auth-Token", token), new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()) };
		try {
			byte[] fileData = FileUtils.readFileToByteArray(new File(formFile));
			String fileBase64Str = Base64.encodeBase64String(fileData);
			JSONObject json = new JSONObject();
			json.put("image", fileBase64Str);
			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");

			// 2.传入二维码识别服务对应的参数, 使用POST方法调用服务并解析输出识别结果
			HttpResponse response = HttpClientUtils.post(url, headers, stringEntity);
			System.out.println(response);
			String content = IOUtils.toString(response.getEntity().getContent());
			System.out.println(content);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 车牌识别，使用Base64编码后的文件方式，使用Token认证方式访问服务
	 * @param token token认证串
	 * @param formFile 文件路径
	 * @throws IOException
	 */
	public static void requestOcrPlateNumberBase64(String token, String formFile) {

		// 1.构建车牌识别服务所需要的参数
		String url = "https://ais.cn-north-1.myhuaweicloud.com/v1.0/ocr/plate-number";
		Header[] headers = new Header[] {new BasicHeader("X-Auth-Token", token), new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()) };
		try {
			byte[] fileData = FileUtils.readFileToByteArray(new File(formFile));
			String fileBase64Str = Base64.encodeBase64String(fileData);
			JSONObject json = new JSONObject();
			json.put("image", fileBase64Str);
			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");

			// 2.传入车牌识别服务对应的参数, 使用POST方法调用服务并解析输出识别结果
			HttpResponse response = HttpClientUtils.post(url, headers, stringEntity);
			System.out.println(response);
			String content = IOUtils.toString(response.getEntity().getContent());
			System.out.println(content);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
    /**
	 * 通用文字识别，使用Base64编码后的文件方式，使用Token认证方式访问服务
	 * @param token token认证串
	 * @param formFile 文件路径
	 * @throws IOException
	 */
	public static void requestOcrGeneralTextBase64(String token, String formFile) {

		// 1.构建通用文字识别服务所需要的参数
		String url = "https://ais.cn-north-1.myhuaweicloud.com/v1.0/ocr/general-text";
		Header[] headers = new Header[] {new BasicHeader("X-Auth-Token", token), new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString()) };
		try {
			byte[] fileData = FileUtils.readFileToByteArray(new File(formFile));
			String fileBase64Str = Base64.encodeBase64String(fileData);
			JSONObject json = new JSONObject();
			json.put("image", fileBase64Str);

			// 
			// 1.a 此项参数可选，可以指定是否检测文字方向，不填则默认不检测文字方向，
			// detect_direction可选:true, false
			//
			json.put("detect_direction", false);


			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");

			// 2.传入通用文字识别服务对应的参数, 使用POST方法调用服务并解析输出识别结果
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
		
		String username = "zhangsan";    // 此处，请输入用户名
		String password = "***";	  // 此处，请输入对应用户名的密码
		String regionName = "cn-north-1"; // 此处，请输入服务的区域信息，参考地址: http://developer.huaweicloud.com/endpoint.html
		String token = getToken(username, password, regionName);
		System.out.println(token);
		
		// 运行增值税发票识别服务，请调用方法requestOcrVatInvoiceBase64
		//requestOcrVatInvoiceBase64(token, "data/vat-invoice-demo.jpg");

		// 运行英文海关单据识别服务，请调用方法requestOcrCustomsFormEnBase64
		//requestOcrCustomsFormEnBase64(token, "data/customs-form-en-demo.jpg");

		// 运行身份证识别服务，请调用方法requestOcrIDCardBase64
		//requestOcrIDCardBase64(token, "data/id-card-demo.png");

		// 运行驾驶证识别服务，请调用方法requestOcrDriverLicenseBase64
		//requestOcrDriverLicenseBase64(token, "data/driver-license-demo.png");

		// 运行行驶证识别服务，请调用方法requestOcrVehicleLicenseBase64
		//requestOcrVehicleLicenseBase64(token, "data/vehicle-license-demo.png");

		// 运行通用表格识别服务，请调用方法requestOcrGeneralTableBase64
		//requestOcrGeneralTableBase64(token, "data/general-table-demo.png");

		// 运行手写体识别服务，请调用方法requestOcrHandwritingBase64
		//requestOcrHandwritingBase64(token, "data/handwriting-demo.jpg");
		
		// 运行银行卡识别服务，请调用方法requestOcrBankCardBase64
		//requestOcrBankCardBase64(token, "data/bank-card-demo.png");

		// 运行机动车购车发票识别服务，请调用方法requestOcrMvsInvoiceBase64
		//requestOcrMvsInvoiceBase64(token, "data/mvs-invoice-demo.jpg");
		
		// 运行一维码识别服务，请调用方法requestOcrBarcodeBase64
		//requestOcrBarcodeBase64(token, "data/barcode-demo.jpg");

		// 运行二维码识别服务，请调用方法requestOcrQRCodeBase64
		//requestOcrQRCodeBase64(token, "data/qr-code-demo.jpg");
		
		// 运行车牌识别服务，请调用方法requestOcrPlateNumberBase64
		//requestOcrPlateNumberBase64(token, "data/plate-number-demo.jpg");

		// 运行通用文字识别服务，请调用方法requestOcrGeneralTextBase64
		//requestOcrGeneralTextBase64(token, "data/general-text-demo.jpg");
	}

}
