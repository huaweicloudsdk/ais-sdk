<?php
/**
 * 图片反黄检测服务ak,sk 方式请求的示例
 */
require "./ais_sdk/image_antiporn.php";
require "./ais_sdk/utils.php";

// 当前支持北京1：cn-north-1 和香港：ap-southeast-1 等region信息
init_region($region = 'cn-north-1');
$app_key = "*************";
$app_secret = "*************";

$filepath = "./data/moderation-antiporn.jpg";
$data = file_to_base64($filepath);

$data_url = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg";

// 图片的base64 的方式请求接口
$result = image_antiporn_aksk($app_key, $app_secret, $data, "");
echo $result;

// 图片的osb的url 方式请求接口
$result = image_antiporn_aksk($app_key, $app_secret, "", $data_url);
echo $result;
