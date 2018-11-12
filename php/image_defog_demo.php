<?php
/**
 * 图像去雾服务示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/image_defog.php";
require "./ais_sdk/utils.php";

$filepath = "./data/defog-demo.png";
$image = fileToBase64($filepath);

/**
 * token 方式请求
 */
$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名
$regionName = "********";    // 配置区域信息

$token = gettoken($username, $password, $domainName, $regionName);

$result = image_defog($token, $image, 1.5, true);
print_r($result);
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"];
base64ToFile("data/defog-demo-token.png", $basestr);

/**
 * ak,sk 方式请求
 */
$app_key = "*************";
$app_secret = "*************";

$result = image_defog_aksk($app_key, $app_secret, $image, 1.5, true);
print_r($result);
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"];
base64ToFile("data/defog-demo-aksk.png", $basestr);
