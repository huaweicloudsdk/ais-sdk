<?php
/**
 * 背景音乐识别服务的ak,sk请求方式示例
 */

require "./ais_sdk/asr_bgm.php";
require "./ais_sdk/utils.php";

// 当前支持北京1：cn-north-1 和香港：ap-southeast-1 等region信息
init_region($region = 'cn-north-1');
$app_key = "*************";
$app_secret = "*************";

// obs链接需要和region区域一致，不同的region的obs资源不共享
$data_url = "https://obs-test-llg.obs.cn-north-1.myhuaweicloud.com/bgm_recognition";

$result = asr_bgm_aksk($app_key, $app_secret, $data_url);
echo $result;
$resultobj = json_decode($result, true);
echo $resultobj["result"]["audio_name"];

