<?php
/**
 * 图片清晰度检测的服务ak,sk 方式请求的使用示例
 */
require "./ais_sdk/clarity_detect.php";
require "./ais_sdk/utils.php";

$app_key = "*************";
$app_secret = "*************";
$regionName = "*************";

$filepath = "./data/moderation-clarity-detect.jpg";
$data = fileToBase64($filepath);

$data_url = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/vat-invoice.jpg";

// base64 方式请求
$result = clarity_detect_aksk($regionName, $app_key, $app_secret, $data, "", 0.8);
echo $result;


// obs的url方式请求
$result = clarity_detect_aksk($regionName, $app_key, $app_secret, "", $data_url, 0.8);
echo $result;
