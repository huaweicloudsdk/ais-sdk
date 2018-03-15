package com.huawei.ais.demo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cloud.sdk.util.Base64;
import com.huawei.ais.sdk.util.HttpClientUtils;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import javax.swing.plaf.synth.SynthTextAreaUI;

/**
 * 访问服务返回结果信息验证的工具类
 */
public class ResponseProcessUtils {

	/**
	 * 打印响应的状态码，并检测其值是否为200
	 *
	 * @param response
	 * 				响应结果
	 * @return 结果为200返回true，否则返回false
	 */
	public static boolean isOKResponded(HttpResponse response) {
		int statusCode = response.getStatusLine().getStatusCode();
		System.out.println(statusCode);
		return HttpStatus.SC_OK == statusCode;
	}

	/**
	 * 打印出服务访问完成后，转化为文本的字符流，主要用于JSON数据的展示
	 *
	 * @param response 响应对象
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 */
	public static void processResponse(HttpResponse response) throws UnsupportedOperationException, IOException {
		System.out.println(HttpClientUtils.convertStreamToString(response.getEntity().getContent()));
	}

	/**
	 *  将字节数组写入到文件, 用于支持二进制文件的生成
	 * @param fileName 文件名
	 * @param data 数据
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static void writeBytesToFile(String fileName, byte[] data) throws IOException{

		FileChannel fc = null;
		try {
			ByteBuffer bb = ByteBuffer.wrap(data);
			fc = new FileOutputStream(fileName).getChannel();
			fc.write(bb);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		finally {
			fc.close();
		}
	}

	/**
	 * 处理返回Base64编码的语音文件的生成
	 *
	 * @param response
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 */
	public static void processResponseWithWavData(HttpResponse response, String fileName) throws UnsupportedOperationException, IOException {
		String result = HttpClientUtils.convertStreamToString(response.getEntity().getContent());
		JSONObject resp = JSON.parseObject(result);
		JSONObject data = (JSONObject) resp.get("result");
		String wavString = (String) data.get("data");
		byte[] fileBytes = Base64.decode(wavString);
		writeBytesToFile(fileName, fileBytes);
	}

	public static void printResponse(HttpResponse response) throws ParseException, IOException {
		StringBuilder stringBuilder = new StringBuilder("")
				.append("response code: ")
				.append(response.getStatusLine().getStatusCode())
				.append(" ")
				.append(response.getStatusLine().getReasonPhrase())
				.append("\n")
				.append(headersToString(response.getAllHeaders()))
				.append("\n")
				.append("response data: ")
				.append(EntityUtils.toString(response.getEntity(), ContentType.APPLICATION_JSON.getCharset()));
		System.out.println(stringBuilder);
		System.out.println("-----------------------------------\n\n");
	}

	private static String headersToString(Header[] headers){
		StringBuilder stringBuilder = new StringBuilder().append("[\n");
		for (Header header : headers)
		{
			stringBuilder.append("  ")
					.append(header.getName())
					.append(":")
					.append(header.getValue())
					.append("\n");
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}
}


