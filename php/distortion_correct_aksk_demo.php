<?php
/**
 * 扭曲矫正服务ak.sk 方式请求的示例
 */

require "./ais_sdk/distortion_correct.php";
require "./ais_sdk/utils.php";

$app_key = "*************";
$app_secret = "*************";
initRegion($region = "cn-north-1");

$filepath = "./data/modeation-distortion.jpg";
$image = fileToBase64($filepath);

$demo_data_url = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/vat-invoice.jpg";

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

