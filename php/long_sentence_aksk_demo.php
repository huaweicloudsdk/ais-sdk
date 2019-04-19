<?php
/**
 * 语音识别服务ak,sk 方式请求的使用示例
 */
require "./ais_sdk/long_sentence.php";
require "./ais_sdk/utils.php";

// region目前支持华北-北京一(cn-north-1)
init_region($region = 'cn-north-1');

$app_key = "*************";
$app_secret = "*************";

$filepath = "./data/asr-sentence.wav";
$data = file_to_base64($filepath);

$data_url = "https://obs-ch-sdk-sample.obs.cn-north-1.myhwclouds.com/lsr-1.mp3";

// base64 方式请求
$result = long_sentence_aksk($app_key, $app_secret, $data, "");
echo $result;
echo "\n";

// obs的url方式请求
$result = long_sentence_aksk($app_key, $app_secret, "", $data_url);
echo $result;

