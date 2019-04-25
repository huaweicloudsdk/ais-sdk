<?php
require "signer.php";
require "ais.php";

/**
 * token 方式
 */
function image_defog($token, $data, $gamma, $natural_look)
{

    $endPoint = get_endpoint(IMAGE);
    // 构建请求信息
    $_url = "https://" . $endPoint . DEFOG;

    $data = array(
        "image" => $data,                    // image: 图片信息 base64
        "gamma" => $gamma,                   // gamma：矫正值
        "natural_look" => $natural_look      // natural_look:可选 ，是否保持自然感官 true or false
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

        // 验证服务调用返回的状态是否成功，如果为2xx, 为成功, 否则失败。
        if (status_success($status)) {
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
function image_defog_aksk($_ak, $_sk, $data, $gamma, $natural_look)
{
    // 构建ak，sk对象
    $signer = new Signer();
    $signer->AppKey = $_ak;             // 构建ak
    $signer->AppSecret = $_sk;          // 构建sk

    $endPoint = get_endpoint(IMAGE);

    // 构建请求对象
    $req = new Request();
    $req->method = "POST";
    $req->scheme = "https";
    $req->host = $endPoint;
    $req->uri = DEFOG;

    $data = array(
        "image" => $data,                    // image: 图片信息 base64
        "gamma" => $gamma,                   // gamma：矫正值
        "natural_look" => $natural_look      // natural_look:可选 ，是否保持自然感官 true or false
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

        // 验证服务调用返回的状态是否成功，如果为2xx, 为成功, 否则失败。
        if (status_success($status)) {
            return $response;
        } else {

            echo "Http status is: " . $status . "\n";
            echo $response;
        }

    }
    curl_close($curl);
}
