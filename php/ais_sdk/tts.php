<?php
require "signer.php";
require "ais.php";

/**
 * token 方式
 */
function tts($token, $text, $voice_name = "xiaoyan", $volume = "0", $sample_rate = "16k", $speech_speed = "0", $pitch_rate = "0")
{

    // 构建请求信息
    $_url = "https://" . ENDPOINT . TTS;

    $data = array(
        "text" => $text,                        // text :待合成的文本
        "voice_name" => $voice_name,            // voice_name:合成的声音人员表示
        "volume" => $volume,                    // volume：音量
        "sample_rate" => $sample_rate,          // sample_rate：语音的采样率，目前支持16k，代表16HZ
        "speech_speed" => $speech_speed,        // speech_speed：语速 [-500,500]
        "pitch_rate" => $pitch_rate             // pitch_rate：音高 [-20,20]
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
            return $response;
        } else {
            return "Process the tts result failed!";
        }
        echo $status . "\n";
        echo $response;
    }
    curl_close($curl);


}

/**
 * ak,sk 方式
 */
function tts_aksk($_ak, $_sk, $text, $voice_name = "xiaoyan", $volume = "0", $sample_rate = "16k", $speech_speed = "0", $pitch_rate = "0")
{
    // 构建ak，sk对象
    $signer = new Signer();
    $signer->AppKey = $_ak;             // 构建ak
    $signer->AppSecret = $_sk;          // 构建sk

    // 构建请求对象
    $req = new Request();
    $req->method = "POST";
    $req->scheme = "https";
    $req->host = ENDPOINT;
    $req->uri = TTS;

    $data = array(
        "text" => $text,                        // text :待合成的文本
        "voice_name" => $voice_name,            // voice_name:合成的声音人员表示
        "volume" => $volume,                    // volume：音量
        "sample_rate" => $sample_rate,          // sample_rate：语音的采样率，目前支持16k，代表16HZ
        "speech_speed" => $speech_speed,        // speech_speed：语速 [-500,500]
        "pitch_rate" => $pitch_rate             // pitch_rate：音高 [-20,20]
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
            return $response;
        } else {

            echo $status . "\n";
            echo $response;
            return "Process the tts result failed!";
        }

    }
    curl_close($curl);
}
