<?php
/**
 * 背景音乐识别服务的ak,sk请求方式示例
 */

require "./ais_sdk/asr_bgm.php";
require "./ais_sdk/utils.php";

$app_key = "*************";
$app_secret = "*************";

$data_url = "https://obs-test-llg.obs.cn-north-1.myhuaweicloud.com/bgm_recognition";

$result = asr_bgm_aksk($app_key, $app_secret, $data_url);
echo $result;
$resultobj = json_decode($result, true);
echo $resultobj["result"]["audio_name"];

