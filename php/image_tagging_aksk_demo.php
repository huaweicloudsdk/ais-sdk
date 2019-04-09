<?php
/**
 * 图片标签内容服务ak,sk 方式请求的示例
 */
require "./ais_sdk/image_tagging.php";
require "./ais_sdk/utils.php";

// 当前支持北京1：cn-north-1 和香港：ap-southeast-1 等region信息
init_region($region = 'cn-north-1');
$app_key = "*************";
$app_secret = "*************";

$filepath = "./data/image-tagging-demo.jpg";
$data = file_to_base64($filepath);

// obs链接需要和region区域一致，不同的region的obs资源不共享
$data_url = "https://ais-sample-data.obs.myhuaweicloud.com/tagging-normal.jpg";

// 图片的base64 的方式请求接口
$result = image_tagging_aksk($app_key, $app_secret, $data, "", 5, "en", 2);
echo $result;

// 图片的osb的url 方式请求接口
$result = image_tagging_aksk($app_key, $app_secret, "", $data_url, 5, "en", 2);
echo $result;
