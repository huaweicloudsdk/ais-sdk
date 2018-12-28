<?php
/**
 * 扭曲矫正服务token 方式请求的服务示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/distortion_correct.php";
require "./ais_sdk/utils.php";

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名
$regionName = "********";    // 配置区域信息

$filepath = "./data/modeation-distortion.jpg";
$image = fileToBase64($filepath);

$demo_data_url = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/vat-invoice.jpg";

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
