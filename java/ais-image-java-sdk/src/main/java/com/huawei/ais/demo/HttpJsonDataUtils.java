package com.huawei.ais.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * http数据转换工具，适用于请求响应均为Json格式的情况
 */
public class HttpJsonDataUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String requestToString(HttpRequestBase httpReq) throws ParseException, IOException {
        final StringBuilder builder = new StringBuilder("\n")
                .append(httpReq.getMethod())
                .append(" ")
                .append(httpReq.getURI())
                .append(headersToString(httpReq.getAllHeaders()));
        if (httpReq instanceof HttpEntityEnclosingRequestBase) {
            builder.append("request body:").append(entityToPrettyString(((HttpEntityEnclosingRequestBase) httpReq)
                    .getEntity()));
        }
        return builder.toString();
    }

    public static String responseToString(HttpResponse response) throws IOException {
        final StringBuilder builder = new StringBuilder("\n")
                .append("response code: ")
                .append(response.getStatusLine().getStatusCode())
                .append(" ")
                .append(response.getStatusLine().getReasonPhrase())
                .append(headersToString(response.getAllHeaders()))
                .append("response data:")
                .append(entityToPrettyString(response.getEntity()));
        return builder.toString();
    }

    /**
     * 打印响应的状态码，并检测其值是否为200
     *
     * @param response 响应结果
     * @return 状态码在[200, 300)之间返回true，否则返回false
     */
    public static boolean isOKResponded(HttpResponse response) {
        final int statusCode = response.getStatusLine().getStatusCode();
        return statusCode >= 200 && statusCode < 300;
    }

    public static <T> T getResponseObject(HttpResponse response, Class<T> responseType) throws IOException {
        if (!isOKResponded(response)) {
            throw new IllegalArgumentException("the response status is not '200 OK'");
        }
        return objectMapper.readValue(EntityUtils.toString(response.getEntity(),
                ContentType.APPLICATION_JSON.getCharset()), responseType);
    }

    public static <T> T getResponseObject(HttpResponse response, Class<T> responseType, String jsonRootName)
            throws IOException {
        if (!isOKResponded(response)) {
            throw new IllegalArgumentException("the response status code is not in [200, 300)!");
        }
        ObjectReader reader = objectMapper.readerFor(responseType).withRootName(jsonRootName);
        return reader.readValue(EntityUtils.toString(response.getEntity(), ContentType.APPLICATION_JSON.getCharset()));
    }

    public static String ObjectToJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String ObjectToPrettyJsonString(Object object) {
        try {
            return "\n" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object) + "\n";
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 格式化json
     *
     * @param jsonString json串
     * @return 格式化后的json
     * @throws IllegalStateException
     */
    public static String prettify(String jsonString) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readValue(jsonString, Object.class));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String entityToPrettyString(HttpEntity entity) throws IOException {
        final String jsonStr = EntityUtils.toString(entity, ContentType.APPLICATION_JSON.getCharset());
        return "\n" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                objectMapper.readValue(jsonStr, JsonNode.class)) + "\n";
    }

    public static HttpEntity ObjectToHttpEntity(Object object) {
        try {
            return new StringEntity(objectMapper.writeValueAsString(object), ContentType.APPLICATION_JSON.getCharset());
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String headersToString(final Header[] headers) {
        final StringBuilder builder = new StringBuilder("\n").append("[\n");
        for (final Header header : headers) {
            builder.append("  ")
                    .append(header.getName())
                    .append(":")
                    .append(header.getValue())
                    .append("\n");
        }
        builder.append("]\n");
        return builder.toString();
    }
}
