<?php
/**
 * 图片反黄检测服务token 方式请求的示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/image_antiporn.php";
require "./ais_sdk/utils.php";

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名
$regionName = "********";    // 配置区域信息

$filepath = "./data/moderation-antiporn.jpg";
$data = fileToBase64($filepath);

$data_url = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg";

$token = gettoken($username, $password, $domainName, $regionName);

// 图片base64方式请求接口
$result = image_antiporn($token, $data, "");
echo $result;

// 图片的obs 的url方式请求接口
$result = image_antiporn($token, "", $data_url);
echo $result;

