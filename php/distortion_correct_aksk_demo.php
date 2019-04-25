<?php
/**
 * 扭曲矫正服务ak.sk 方式请求的示例
 */

require "./ais_sdk/distortion_correct.php";
require "./ais_sdk/utils.php";

// region目前支持华北-北京一(cn-north-1)、亚太-香港(ap-southeast-1)
init_region($region = 'cn-north-1');

$app_key = "*************";
$app_secret = "*************";

$filepath = "./data/modeation-distortion.jpg";
$image = file_to_base64($filepath);

$demo_data_url = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/vat-invoice.jpg";

// ak,sk 方式图片的base64请求接口
$result = distortion_correct_aksk($app_key, $app_secret, $image, "", true);
print_r($result);
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"]['data'];
if ($basestr != "") {
    base64_to_file("data/moderation_distortion-aksk-1.jpg", $basestr);
}

// ak,sk 方式图片的url方式请求接口
$result = distortion_correct_aksk($app_key, $app_secret, "", $demo_data_url, true);
print_r($result);
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"]['data'];
if ($basestr != "") {
    base64_to_file("data/moderation_distortion-aksk-2.jpg", $basestr);
}

