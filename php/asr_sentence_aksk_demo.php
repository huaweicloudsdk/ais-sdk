<?php
/**
 * 语音识别服务的使用示例
 */
require "./ais_sdk/asr_sentence.php";
require "./ais_sdk/utils.php";

// region目前支持华北-北京一(cn-north-1)
init_region($region = 'cn-north-1');

$app_key = "*************";
$app_secret = "*************";

$filepath = "./data/asr-sentence.wav";
$data = file_to_base64($filepath);

$data_url = "https://obs-ch-sdk-sample.obs.cn-north-1.myhuaweicloud.com/asr-sentence.wav";

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

