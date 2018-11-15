<?php
/**
 * 图片标签内容服务ak,sk 方式请求的示例
 */
require "./ais_sdk/image_tagging.php";
require "./ais_sdk/utils.php";

$app_key = "*************";
$app_secret = "*************";

$filepath = "./data/image-tagging-demo.jpg";
$data = fileToBase64($filepath);

$data_url = "https://ais-sample-data.obs.myhwclouds.com/tagging-normal.jpg";

// 图片的base64 的方式请求接口
$result = image_tagging_aksk($app_key, $app_secret, $data, "", 5, "en", 2);
echo $result;

// 图片的osb的url 方式请求接口
$result = image_tagging_aksk($app_key, $app_secret, "", $data_url, 5, "en", 2);
echo $result;
