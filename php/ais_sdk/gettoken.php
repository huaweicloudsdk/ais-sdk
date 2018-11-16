<?php
/**
 * 获取token 信息
 */
function gettoken($username, $password, $domainName, $regionName)
{
    $requestBody = requestBodyForGetToken($username, $password, $domainName, $regionName);
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

    // 获取响应头的信息
    $headerSize = curl_getinfo($curl, CURLINFO_HEADER_SIZE);
    $headers = substr($response, 0, $headerSize);

    curl_close($curl);
    return gettokenByheaders($headers);
}

/**
 * 拼接json信息，获得请求体信息
 * @param $username 用户名
 * @param $password 密码
 * @param $domain 与用户名一致
 * @param $regionName 区域名称
 */
function requestBodyForGetToken($username, $password, $domainName, $regionName)
{
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
function gettokenByheaders($headers)
{

    $headArr = explode("\r\n", $headers);
    foreach ($headArr as $loop) {
        if (strpos($loop, "X-Subject-Token") !== false) {
            $token = trim(substr($loop, 17));
            return $token;
        }
    }
}

