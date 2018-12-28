<?php
/**
 * 语音识别服务ak,sk 方式请求的使用示例
 */
require "./ais_sdk/long_sentence.php";
require "./ais_sdk/utils.php";

$app_key = "*************";
$app_secret = "*************";

$filepath = "./data/asr-sentence.wav";
$data = fileToBase64($filepath);

$data_url = "https://ais-sample-data.obs.myhuaweicloud.com/lsr-1.mp3";

// base64 方式请求
$result = long_sentence_aksk($app_key, $app_secret, $data, "");
echo $result;
echo "\n";

// obs的url方式请求
$result = long_sentence_aksk($app_key, $app_secret, "", $data_url);
echo $result;

