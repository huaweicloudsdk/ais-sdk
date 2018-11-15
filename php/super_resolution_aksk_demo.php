<?php
/**
 * 图像超分重建服务ak,sk 方式请求的示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/super_resolution.php";
require "./ais_sdk/utils.php";

$app_key = "*************";
$app_secret = "*************";

$filepath = "./data/super-resolution-demo.png";
$data = fileToBase64($filepath);

$data_url = "https://ais-sample-data.obs.myhwclouds.com/recapture-detect.jpg";

$result = super_resolution_aksk($app_key, $app_secret, $data, 3, "ESPCN");
echo $result;
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"];
base64ToFile("data/super-resolution-aksk.png", $basestr);
