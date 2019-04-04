<?php
/**
 * 图像去雾服务aksk 方式请求的示例
 */
require "./ais_sdk/image_defog.php";
require "./ais_sdk/utils.php";

$app_key = "*************";
$app_secret = "*************";
initRegion($region = "cn-north-1");

$filepath = "./data/defog-demo.png";
$image = fileToBase64($filepath);

$result = image_defog_aksk($app_key, $app_secret, $image, 1.5, true);
print_r($result);
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"];
base64ToFile("data/defog-demo-aksk.png", $basestr);
