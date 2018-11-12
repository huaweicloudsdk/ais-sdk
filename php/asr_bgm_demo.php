<?php
/**
 * 背景音乐识别服务的示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/asr_bgm.php";
require "./ais_sdk/utils.php";

/**
 * token 方式请求
 */
$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名
$regionName = "********";    // 配置区域信息

$token = gettoken($username, $password, $domainName, $regionName);

$data_url = "https://obs-test-llg.obs.cn-north-1.myhwclouds.com/bgm_recognition";

$result = asr_bgm($token, $data_url);
echo $result;
$resultobj = json_decode($result, true);
echo $resultobj["result"]["audio_name"];

/**
 * ak,sk 方式请求
 */

$app_key = "*************";
$app_secret = "*************";

$result = asr_bgm_aksk($app_key, $app_secret, $data_url);
echo $result;
$resultobj = json_decode($result, true);
echo $resultobj["result"]["audio_name"];

