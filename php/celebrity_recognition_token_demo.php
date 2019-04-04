<?php
/**
 * 名人识别检测服务的token 方式请求示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/celebrity_recognition.php";
require "./ais_sdk/utils.php";

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名
initRegion($region = "cn-north-1");

$filepath = "./data/celebrity-recognition.jpg";
$data = fileToBase64($filepath);

// obs链接需要和region区域一致，不同的region的obs资源不共享
$data_url = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/celebrity-recognition.jpg";

$token = gettoken($username, $password, $domainName);

// 图片base64方式请求接口
$result = celebrity_recognition($token, $data, "");
echo $result;

// 图片的obs 的url方式请求接口
$result = celebrity_recognition($token, "", $data_url);
echo $result;

