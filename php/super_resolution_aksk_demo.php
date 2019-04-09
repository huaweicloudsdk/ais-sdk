<?php
/**
 * 图像超分重建服务ak,sk 方式请求的示例
 */
require "./ais_sdk/super_resolution.php";
require "./ais_sdk/utils.php";

// 当前支持北京1：cn-north-1 和香港：ap-southeast-1 等region信息
init_region($region = 'cn-north-1');
$app_key = "*************";
$app_secret = "*************";

$filepath = "./data/super-resolution-demo.png";
$data = file_to_base64($filepath);

$result = super_resolution_aksk($app_key, $app_secret, $data, 3, "ESPCN");
echo $result;
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"];
base64_to_file("data/super-resolution-aksk.png", $basestr);
