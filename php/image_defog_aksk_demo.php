<?php
/**
 * 图像去雾服务aksk 方式请求的示例
 */
require "./ais_sdk/image_defog.php";
require "./ais_sdk/utils.php";

// region目前支持华北-北京一(cn-north-1)、亚太-香港(ap-southeast-1)
init_region($region = 'cn-north-1');

$app_key = "*************";
$app_secret = "*************";

$filepath = "./data/defog-demo.png";
$image = file_to_base64($filepath);

$result = image_defog_aksk($app_key, $app_secret, $image, 1.5, true);
print_r($result);
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"];
base64_to_file("data/defog-demo-aksk.png", $basestr);
