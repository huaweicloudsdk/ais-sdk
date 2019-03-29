<?php
require "signer.php";
require "ais.php";

/**
 * token 方式
 */
function image_tagging($regionName, $token, $image, $url, $threshold, $language, $limit = -1)
{

    // 构建请求信息
    $_url = "https://" . strtr(IMAGE_ENDPOINT, array(REPLACE_ENDPOINT => $regionName)) . IMAGE_TAGGING;

    $data = array(
        "language" => $language,               // 返回标签的语言类型为zh，中文
        "threshold" => $threshold,             // 置信度的阈值（0~100），低于此置信数的标签，将不会返回。默认值：0
        "limit" => $limit,                     // tag数，默认值： -1，表示返回所有
    );

    if($image != ""){
        $data["image"] = $image;               // 与url二选一 图片文件Base64编码字符串
    }

    if($url != ""){
        $data["url"] = $url;                   // 与image二选一 图片的URL路径
    }

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
function image_tagging_aksk($regionName, $_ak, $_sk, $image, $url, $threshold, $language, $limit = -1)
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
    $req->uri = IMAGE_TAGGING;

    $data = array(
        "language" => $language,               // 返回标签的语言类型为zh，中文
        "threshold" => $threshold,             // 置信度的阈值（0~100），低于此置信数的标签，将不会返回。默认值：0
        "limit" => $limit,                     // tag数，默认值： -1，表示返回所有
    );

    if($image != ""){
        $data["image"] = $image;               // 与url二选一 图片文件Base64编码字符串
    }

    if($url != ""){
        $data["url"] = $url;                   // 与image二选一 图片的URL路径
    }


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
