<?php
require "signer.php";
require "ais.php";

/**
 * token 方式
 */
function super_resolution($regionName, $token, $data, $scale = 3, $model = "ESPCN")
{

    // 构建请求信息
    $_url = "https://" . strtr(IMAGE_ENDPOINT, array(REPLACE_ENDPOINT => $regionName)) . SURPER_RESOLUTION;

    $data = array(
        "image" => $data,                     // 图片文件BASE64编码字符串
        "file" => "",                         // 文件内容,与image二选一
        "scale" => $scale,                    // 放大倍数，默认为3，取值范围：3或4
        "model" => $model                     // 图像超分辨率重建采用的算法模式，支持ESPCN和SRCNN，默认ESPCN。
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
            echo "Http status is: " . $status . "\n";
            echo $response;
        }

    }
    curl_close($curl);


}

/**
 * ak,sk 方式
 */
function super_resolution_aksk($regionName, $_ak, $_sk, $data, $scale = 3, $model = "ESPCN")
{
    // 构建ak，sk对象
    $signer = new Signer();
    $signer->AppKey = $_ak;             // 构建ak
    $signer->AppSecret = $_sk;          // 构建sk

    // 构建请求对象
    $req = new Request();
    $req->method = "POST";
    $req->scheme = "https";
    $req->host = strtr(IMAGE_ENDPOINT, array(REPLACE_ENDPOINT => $regionName));
    $req->uri = SURPER_RESOLUTION;

    $data = array(
        "image" => $data,                     // 图片文件BASE64编码字符串
        "file" => "",                         // 文件内容,与image二选一
        "scale" => $scale,                    // 放大倍数，默认为3，取值范围：3或4
        "model" => $model                     // 图像超分辨率重建采用的算法模式，支持ESPCN和SRCNN，默认ESPCN。
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

            echo "Http status is: " . $status . "\n";
            echo $response;
        }

    }
    curl_close($curl);
}
