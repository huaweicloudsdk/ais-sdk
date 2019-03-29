<?php
/**
 * 图片内容检测服务ak,sk 方式请求的示例
 */
require "./ais_sdk/image_moderation.php";
require "./ais_sdk/utils.php";

$app_key = "*************";
$app_secret = "*************";
$regionName = "*************";

$filepath = "./data/moderation-terrorism.jpg";
$data = fileToBase64($filepath);

$data_url = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg";

// 图片的base64 的方式请求接口
$result = image_content_aksk($regionName, $app_key, $app_secret, $data, "", array("politics"), 0);
echo $result;

// 图片的osb的url 方式请求接口
$result = image_content_aksk($regionName, $app_key, $app_secret, "", $data_url, array("politics"), 0);
echo $result;
