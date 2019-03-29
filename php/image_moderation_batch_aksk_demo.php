<?php
/**
 * 图片内容检测批量服务ak,sk 方式请求的示例
 */
require "./ais_sdk/image_moderation_batch.php";
require "./ais_sdk/utils.php";

$app_key = "*************";
$app_secret = "*************";
$regionName = "*************";

$data_url1 = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg";
$data_url2 = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg";

$result = image_content_batch_aksk($regionName,$app_key, $app_secret, array($data_url1, $data_url2), array("politics", "terrorism", "porn"), 0);
echo $result;