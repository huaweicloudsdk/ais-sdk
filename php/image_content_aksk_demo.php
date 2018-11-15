<?php
/**
 * 图片内容检测服务ak,sk 方式请求的示例
 */
require "./ais_sdk/image_content.php";
require "./ais_sdk/utils.php";

$app_key = "*************";
$app_secret = "*************";

$filepath = "./data/moderation-terrorism.jpg";
$data = fileToBase64($filepath);

$data_url = "https://ais-sample-data.obs.cn-north-1.myhwclouds.com/terrorism.jpg";

// 图片的base64 的方式请求接口
$result = image_content_aksk($app_key, $app_secret, $data, "", array("politics"), "");
echo $result;

// 图片的osb的url 方式请求接口
$result = image_content_aksk($app_key, $app_secret, "", $data_url, array("politics"), "");
echo $result;
