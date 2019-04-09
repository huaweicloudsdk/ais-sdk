<?php
/**
 * 图像翻拍识别服务ak,sk 方式请求的示例
 */
require "./ais_sdk/recapture_detect.php";
require "./ais_sdk/utils.php";

// 当前支持北京1：cn-north-1 和香港：ap-southeast-1 等region信息
init_region($region = 'cn-north-1');
$app_key = "*************";
$app_secret = "*************";

$filepath = "./data/recapture-detect-demo.jpg";
$data = file_to_base64($filepath);

$data_url = "https://ais-sample-data.obs.myhuaweicloud.com/recapture-detect.jpg";

// 图片的base64 的方式请求接口
$result = recapture_detect_aksk($app_key, $app_secret, $data, "", 0.99, array("recapture"));
echo $result;

// 图片的osb的url 方式请求接口
$result = recapture_detect_aksk($app_key, $app_secret, "", $data_url, 0.99, array("recapture"));
echo $result;
