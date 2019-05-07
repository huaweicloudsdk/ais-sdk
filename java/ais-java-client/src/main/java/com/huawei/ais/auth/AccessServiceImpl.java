package com.huawei.ais.auth;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.cloud.sdk.DefaultRequest;
import com.cloud.sdk.Request;
import com.cloud.sdk.auth.credentials.BasicCredentials;
import com.cloud.sdk.auth.signer.Signer;
import com.cloud.sdk.auth.signer.SignerFactory;
import com.cloud.sdk.http.HttpMethodName;
import com.huawei.ais.sdk.util.HttpClientUtils;

public class AccessServiceImpl extends AccessService {

	private CloseableHttpClient client = null;

	public AccessServiceImpl(String serviceName, String region, String ak, String sk) {
		super(serviceName, region, ak, sk);
	}

	protected CloseableHttpClient getHttpClient() throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException
	{
		return HttpClientUtils.acceptsUntrustedCertsHttpClient();
	}
	
	private CloseableHttpClient getDefaultHttpClient() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).useTLS().build();
		SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext,
				new AllowAllHostnameVerifier());

		return HttpClients.custom().setSSLSocketFactory(sslSocketFactory).build();
	}
	
	protected boolean useDefaultHttpClient()
	{
		return true;
	}
	
	public HttpResponse access(URL url, Map<String, String> headers, InputStream content, Long contentLength,
			HttpMethodName httpMethod) throws Exception {

		// Make a request for signing.
		Request request = new DefaultRequest();
		try {
			// Set the request address.
			request.setEndpoint(url.toURI());

			String urlString = url.toString();

			String parameters = null;

			if (urlString.contains("?")) {
				parameters = urlString.substring(urlString.indexOf("?") + 1);
				Map parametersmap = new HashMap<String, String>();

				if (null != parameters && !"".equals(parameters)) {
					String[] parameterarray = parameters.split("&");

					for (String p : parameterarray) {
						String key = p.split("=")[0];
						String value = p.split("=")[1];
						parametersmap.put(key, value);
					}
					request.setParameters(parametersmap);
				}
			}

		} catch (URISyntaxException e) {
			// It is recommended to add logs in this place.
			e.printStackTrace();
		}
		// Set the request method.
		request.setHttpMethod(httpMethod);
		if (headers != null) {
			// Add request header information if required.
			request.setHeaders(headers);
		}
		// Configure the request content.
		request.setContent(content);

		// Select an algorithm for request signing.
		Signer signer = SignerFactory.getSigner(serviceName, region);
		// Sign the request, and the request will change after the signing.
		signer.sign(request, new BasicCredentials(this.ak, this.sk));

		// Make a request that can be sent by the HTTP client.
		HttpRequestBase httpRequestBase = createRequest(url, null, request.getContent(), contentLength, httpMethod);
		Map<String, String> requestHeaders = request.getHeaders();
		
		// Put the header of the signed request to the new request.
		for (String key : requestHeaders.keySet()) {
			if (key.equalsIgnoreCase(HttpHeaders.CONTENT_LENGTH.toString())) {
				continue;
			}
			httpRequestBase.addHeader(key, requestHeaders.get(key));
		}

		client = useDefaultHttpClient() ? getDefaultHttpClient() : getHttpClient();
		
		// Send the request, and a response will be returned.
		HttpResponse response = client.execute(httpRequestBase);
		return response;
	}

	/**
	 * Make a request that can be sent by the HTTP client.
	 * 
	 * @param url
	 *            specifies the API access path.
	 * @param header
	 *            specifies the header information to be added.
	 * @param content
	 *            specifies the body content to be sent in the API call.
	 * @param contentLength
	 *            specifies the length of the content. This parameter is optional.
	 * @param httpMethod
	 *            specifies the HTTP method to be used.
	 * @return specifies the request that can be sent by an HTTP client.
	 */
	private static HttpRequestBase createRequest(URL url, Header header, InputStream content, Long contentLength,
			HttpMethodName httpMethod) {

		HttpRequestBase httpRequest;
		if (httpMethod == HttpMethodName.POST) {
			HttpPost postMethod = new HttpPost(url.toString());

			if (content != null) {
				InputStreamEntity entity = new InputStreamEntity(content, contentLength);
				postMethod.setEntity(entity);
			}
			httpRequest = postMethod;
		} else if (httpMethod == HttpMethodName.PUT) {
			HttpPut putMethod = new HttpPut(url.toString());
			httpRequest = putMethod;

			if (content != null) {
				InputStreamEntity entity = new InputStreamEntity(content, contentLength);
				putMethod.setEntity(entity);
			}
		} else if (httpMethod == HttpMethodName.PATCH) {
			HttpPatch patchMethod = new HttpPatch(url.toString());
			httpRequest = patchMethod;

			if (content != null) {
				InputStreamEntity entity = new InputStreamEntity(content, contentLength);
				patchMethod.setEntity(entity);
			}
		} else if (httpMethod == HttpMethodName.GET) {
			httpRequest = new HttpGet(url.toString());
		} else if (httpMethod == HttpMethodName.DELETE) {
			httpRequest = new HttpDelete(url.toString());
		} else if (httpMethod == HttpMethodName.HEAD) {
			httpRequest = new HttpHead(url.toString());
		} else {
			throw new RuntimeException("Unknown HTTP method name: " + httpMethod);
		}

		httpRequest.addHeader(header);
		return httpRequest;
	}

	@Override
	public void close() {
		try {
			if (client != null) {
				client.close();
			}
		} catch (IOException e) {
			// It is recommended to add logs in this place.
			e.printStackTrace();
		}
	}

	@Override
	public HttpResponse accessEntity(URL url, Map<String, String> headers, HttpEntity entity, Long contentLength,
			HttpMethodName httpMethod) throws Exception {
		// Make a request for signing.
		Request request = new DefaultRequest();
		try {
			// Set the request address.
			request.setEndpoint(url.toURI());

			String urlString = url.toString();

			String parameters = null;

			if (urlString.contains("?")) {
				parameters = urlString.substring(urlString.indexOf("?") + 1);
				Map parametersmap = new HashMap<String, String>();

				if (null != parameters && !"".equals(parameters)) {
					String[] parameterarray = parameters.split("&");

					for (String p : parameterarray) {
						String key = p.split("=")[0];
						String value = p.split("=")[1];
						parametersmap.put(key, value);
					}
					request.setParameters(parametersmap);
				}
			}

		} catch (URISyntaxException e) {
			// It is recommended to add logs in this place.
			e.printStackTrace();
		}
		// Set the request method.
		request.setHttpMethod(httpMethod);
		if (headers != null) {
			request.setHeaders(headers);
		}
		// Configure the request content.
		request.setContent(entity.getContent());

		// Select an algorithm for request signing.
		Signer signer = SignerFactory.getSigner(serviceName, region);
		// Sign the request, and the request will change after the signing.
		signer.sign(request, new BasicCredentials(this.ak, this.sk));

		// Make a request that can be sent by the HTTP client.
		HttpRequestBase httpRequestBase = createRequestEntity(url, null, entity, contentLength, httpMethod);
		Map<String, String> requestHeaders = request.getHeaders();
		// Put the header of the signed request to the new request.
		for (String key : requestHeaders.keySet()) {
			if (key.equalsIgnoreCase(HttpHeaders.CONTENT_LENGTH.toString())) {
				continue;
			}
			httpRequestBase.addHeader(key, requestHeaders.get(key));
		}

		client = useDefaultHttpClient() ? getDefaultHttpClient() : getHttpClient();
		HttpResponse response = client.execute(httpRequestBase);
		return response;
	}

	private static HttpRequestBase createRequestEntity(URL url, Header header, HttpEntity entity, Long contentLength,
			HttpMethodName httpMethod) {

		HttpRequestBase httpRequest;
		if (httpMethod == HttpMethodName.POST) {
			HttpPost postMethod = new HttpPost(url.toString());

			if (entity != null) {
				postMethod.setEntity(entity);
			}
			httpRequest = postMethod;
		} else if (httpMethod == HttpMethodName.PUT) {
			HttpPut putMethod = new HttpPut(url.toString());
			httpRequest = putMethod;

			if (entity != null) {
				putMethod.setEntity(entity);
			}
		} else if (httpMethod == HttpMethodName.PATCH) {
			HttpPatch patchMethod = new HttpPatch(url.toString());
			httpRequest = patchMethod;

			if (entity != null) {
				patchMethod.setEntity(entity);
			}
		} else if (httpMethod == HttpMethodName.GET) {
			httpRequest = new HttpGet(url.toString());
		} else if (httpMethod == HttpMethodName.DELETE) {
			httpRequest = new HttpDelete(url.toString());
		} else if (httpMethod == HttpMethodName.HEAD) {
			httpRequest = new HttpHead(url.toString());
		} else {
			throw new RuntimeException("Unknown HTTP method name: " + httpMethod);
		}

		httpRequest.addHeader(header);
		return httpRequest;
	}

}
