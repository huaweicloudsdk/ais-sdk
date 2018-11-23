<?php
/**
 * 语音识别服务的使用示例
 */
require "./ais_sdk/asr_sentence.php";
require "./ais_sdk/utils.php";

$app_key = "*************";
$app_secret = "*************";

$filepath = "./data/asr-sentence.wav";
$data = fileToBase64($filepath);

$data_url = "https://ais-sample-data.obs.myhwclouds.com/asr-sentence.wav";

// base64 方式请求
$result = asr_sentence_aksk($app_key, $app_secret, $data, "", "wav", "16k");
echo $result;
$resultobj = json_decode($result, true);
echo $resultobj["result"]["words"];

// obs的url方式请求
$result = asr_sentence_aksk($app_key, $app_secret, "", $data_url, "wav", "16k");
echo $result;
$resultobj = json_decode($result, true);
echo $resultobj["result"]["words"];

