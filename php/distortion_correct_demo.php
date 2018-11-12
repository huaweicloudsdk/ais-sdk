<?php
/**
 * 扭曲矫正服务服务示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/distortion_correct.php";
require "./ais_sdk/utils.php";

$filepath = "./data/modeation-distortion.jpg";
$image = fileToBase64($filepath);

$demo_data_url = "https://ais-sample-data.obs.cn-north-1.myhwclouds.com/vat-invoice.jpg";
/**
 * token 方式请求
 */
$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名
$regionName = "********";    // 配置区域信息

$token = gettoken($username, $password, $domainName, $regionName);

// token方式 图片base64请求接口
$result = distortion_correct($token, $image, "", true);
print_r($result);
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"]['data'];
if ($basestr != "") {
    base64ToFile("data/moderation_distortion-token-1.jpg", $basestr);
}

// token 方式图片的url请求接口
$result = distortion_correct($token, "", $demo_data_url, true);
print_r($result);
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"]['data'];
if ($basestr != "") {
    base64ToFile("data/moderation_distortion-token-2.jpg", $basestr);
}

/**
 * ak,sk 方式请求
 */
$app_key = "*************";
$app_secret = "*************";

// ak,sk 方式图片的base64请求接口
$result = distortion_correct_aksk($app_key, $app_secret, $image, "", true);
print_r($result);
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"]['data'];
if ($basestr != "") {
    base64ToFile("data/moderation_distortion-aksk-1.jpg", $basestr);
}

// ak,sk 方式图片的url方式请求接口
$result = distortion_correct_aksk($app_key, $app_secret, "", $demo_data_url, true);
print_r($result);
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"]['data'];
if ($basestr != "") {
    base64ToFile("data/moderation_distortion-aksk-2.jpg", $basestr);
}

