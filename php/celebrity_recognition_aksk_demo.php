<?php
/**
 * 名人识别检测服务的ak,sk 方式请求示例
 */
require "./ais_sdk/celebrity_recognition.php";
require "./ais_sdk/utils.php";

// region目前支持华北-北京一(cn-north-1)、亚太-香港(ap-southeast-1)
init_region($region = 'cn-north-1');

$app_key = "*************";
$app_secret = "*************";

$filepath = "./data/celebrity-recognition.jpg";
$data = file_to_base64($filepath);

// obs链接需要和region区域一致，不同的region的obs资源不共享
$data_url = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/celebrity-recognition.jpg";

// 图片的base64 的方式请求接口
$result = celebrity_recognition_aksk($app_key, $app_secret, $data, "");
echo $result;

// 图片的osb的url 方式请求接口
$result = celebrity_recognition_aksk($app_key, $app_secret, "", $data_url);
echo $result;
