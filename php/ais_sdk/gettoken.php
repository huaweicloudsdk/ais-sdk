<?php

/**
 * 获取token 信息
 */
function get_token($username, $password, $domainName)
{
    $requestBody = requestbody_get_token($username, $password, $domainName);
    $_url = "https://" . IAM_ENPOINT . AIS_TOKEN;;
    $curl = curl_init();
    $headers = array(
        "Content-Type" => "application/json",
    );

    // 请求信息封装
    curl_setopt($curl, CURLOPT_URL, $_url);
    curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
    curl_setopt($curl, CURLOPT_POSTFIELDS, $requestBody);
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, TRUE);
    curl_setopt($curl, CURLOPT_NOBODY, FALSE);
    curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);

    //获取响应头的信息，默认不获取
    curl_setopt($curl, CURLOPT_HEADER, true);
    $response = curl_exec($curl);

    $status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
    if ($status == 0) {
        echo curl_error($curl);
    } else {

        // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
        if (substr($status, 0, 1) != 2) {
            echo "Http status is: " . $status . "\n";
            echo $response;
        }
    }

    // 获取响应头的信息
    $headerSize = curl_getinfo($curl, CURLINFO_HEADER_SIZE);
    $headers = substr($response, 0, $headerSize);

    curl_close($curl);
    return get_token_by_headers($headers);
}

/**
 * 拼接json信息，获得请求体信息
 * @param $username 用户名
 * @param $password 密码
 * @param $domain 与用户名一致
 *
 */
function requestbody_get_token($username, $password, $domainName)
{
    $regionName = $GLOBALS["regionName"];

    $param = array(
        "auth" => array(
            "identity" => array(
                "password" => array(
                    "user" => array(
                        "password" => $password,
                        "domain" => array(
                            "name" => $domainName
                        ),
                        "name" => $username
                    )
                ),
                "methods" => array("password")

            ),
            "scope" => array(
                "project" => array(
                    "name" => $regionName
                )
            )
        )
    );
    return urldecode(json_encode($param));

}

/**
 * 根据请求体的信息，获取到token的内容
 * @param $headers
 * @return string
 */
function get_token_by_headers($headers)
{

    $headArr = explode("\r\n", $headers);
    foreach ($headArr as $loop) {
        if (strpos($loop, "X-Subject-Token") !== false) {
            $token = trim(substr($loop, 17));
            return $token;
        }
    }
}

