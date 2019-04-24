<?php
/**
 * 语音合成服务的ak,sk方式请求的示例
 */
require "./ais_sdk/tts.php";
require "./ais_sdk/utils.php";

// region目前支持华北-北京一(cn-north-1)
init_region($region = 'cn-north-1');

$app_key = "*************";
$app_secret = "*************";

// ak,sk默认方式请求数据
$result = tts_aksk($app_key, $app_secret, "语音合成为你的业务增加交互的能力.");
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"]["data"];
base64_to_file("data/tts_use_aksk_default_config.wav", $basestr);

// ak,sk特殊方式请求数据
$result = tts_aksk($app_key, $app_secret, "语音合成为你的业务增加交互的能力.", "xiaoyan", 0, "16k", 0, 0);
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"]["data"];
base64_to_file("data/tts_use_aksk_specific_config.wav", $basestr);