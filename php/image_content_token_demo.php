<?php
/**
 * 图片内容检测服务token 方式请求的示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/image_content.php";
require "./ais_sdk/utils.php";

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名
$regionName = "********";    // 配置区域信息

$filepath = "./data/moderation-terrorism.jpg";
$data = fileToBase64($filepath);

$data_url = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg";

$token = gettoken($username, $password, $domainName, $regionName);

// 图片base64方式请求接口
$result = image_content($token, $data, "", array("politics"), 0);
echo $result;
echo "\n";

// 图片的obs 的url方式请求接口
$result = image_content($token, "", $data_url, array("politics"), 0);
echo $result;