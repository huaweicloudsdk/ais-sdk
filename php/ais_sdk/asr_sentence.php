<?php
require "signer.php";
require "ais.php";

/**
 * token 方式
 */
function asr_sentence($token, $data, $url = "", $encode_type = "wav", $sample_rate = "16k")
{

    // 构建请求信息
    $_url = "https://" . ASR_ENDPOINT . ASR_SENTENCE;

    $data = array(
        "data" => $data,                        // data: 音频文件的base64
        "url" => $url,                          // url: 音频文件的obs的url地址
        "encode_type" => $encode_type,          // encode_type：支持语音的格式
        "sample_rate" => $sample_rate,          // sample_rate：语音的采样率，目前支持16k，代表16HZ
    );

    $curl = curl_init();
    $headers = array(
        "Content-Type:application/json",
        "X-Auth-Token:" . $token);

    // 请求信息封装
    curl_setopt($curl, CURLOPT_URL, $_url);
    curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
    curl_setopt($curl, CURLOPT_POSTFIELDS, json_encode($data));
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, TRUE);
    curl_setopt($curl, CURLOPT_NOBODY, FALSE);
    curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);

    // 执行请求信息
    $response = curl_exec($curl);
    $status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
    if ($status == 0) {
        echo curl_error($curl);
    } else {

        // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
        if ($status == 200) {
            return json_encode(json_decode($response, true),JSON_UNESCAPED_UNICODE);
        } else {
            echo "Http status is: " . $status . "\n";
            echo $response;
        }

    }
    curl_close($curl);


}

/**
 * ak,sk 方式
 */
function asr_sentence_aksk($_ak, $_sk, $data, $url = "", $encode_type = "wav", $sample_rate = "16k")
{
    // 构建ak，sk对象
    $signer = new Signer();
    $signer->AppKey = $_ak;             // 构建ak
    $signer->AppSecret = $_sk;          // 构建sk

    // 构建请求对象
    $req = new Request();
    $req->method = "POST";
    $req->scheme = "https";
    $req->host = ASR_ENDPOINT;
    $req->uri = ASR_SENTENCE;

    $data = array(
        "data" => $data,                        // data: 音频文件的base64
        "url" => $url,                          // url: 音频文件的obs的url地址
        "encode_type" => $encode_type,          // encode_type：支持语音的格式
        "sample_rate" => $sample_rate,          // sample_rate：语音的采样率，目前支持16k，代表16HZ
    );

    $headers = array(
        "Content-Type" => "application/json",
    );

    $req->headers = $headers;
    $req->body = json_encode($data);

    // 获取ak，sk方式的请求对象，执行请求
    $curl = $signer->Sign($req);
    curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);
    $response = curl_exec($curl);
    $status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
    if ($status == 0) {
        echo curl_error($curl);
    } else {

        // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
        if ($status == 200) {
            return json_encode(json_decode($response, true),JSON_UNESCAPED_UNICODE);
        } else {

            echo "Http status is: " . $status . "\n";
            echo $response;
        }

    }
    curl_close($curl);
}
