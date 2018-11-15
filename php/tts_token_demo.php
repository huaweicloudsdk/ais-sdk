<?php
/**
 * 语音合成服务的token方式请求的示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/tts.php";
require "./ais_sdk/utils.php";

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名
$regionName = "********";    // 配置区域信息

$token = gettoken($username, $password, $domainName, $regionName);

// token默认方式请求数据
$result = tts($token, "语音合成为你的业务增加交互的能力.");
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"]["data"];
base64ToFile("data/tts_use_default_config.wav", $basestr);

// token特殊方式请求数据
$result = tts($token, "语音合成为你的业务增加交互的能力.", "xiaoyan", 0, "16k", 0, 0);
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"]["data"];
base64ToFile("data/tts_use_specific_config.wav", $basestr);
