<?php
/**
 * 图片内容检测批量异步服务ak,sk 方式请求的示例
 */
require "./ais_sdk/image_moderation_batch_jobs.php";
require "./ais_sdk/utils.php";

$app_key = "*************";
$app_secret = "*************";
initRegion($region = "cn-north-1");

$data_url1 = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg";
$data_url2 = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg";

$result = batch_jobs_aksk($app_key, $app_secret, array($data_url1,$data_url2), array("politics","terrorism","porn"));
echo json_encode($result);