<?php
/**
 * 图片内容检测服务ak,sk 方式请求的示例
 */
require "./ais_sdk/image_moderation.php";
require "./ais_sdk/utils.php";

// 当前支持北京1：cn-north-1 和香港：ap-southeast-1 等region信息
init_region($region = 'cn-north-1');
$app_key = "*************";
$app_secret = "*************";

$filepath = "./data/moderation-terrorism.jpg";
$data = file_to_base64($filepath);

$data_url = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg";

// 图片的base64 的方式请求接口
$result = image_content_aksk($app_key, $app_secret, $data, "", array("politics"), 0);
echo $result;

// 图片的osb的url 方式请求接口
$result = image_content_aksk($app_key, $app_secret, "", $data_url, array("politics"), 0);
echo $result;
