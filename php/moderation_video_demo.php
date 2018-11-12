<?php
/**
 * 语音识别服务的使用示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/moderation_video.php";
require "./ais_sdk/utils.php";

/**
 * token 方式请求
 */
$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名
$regionName = "********";    // 配置区域信息

$demo_data_url = "https://obs-test-llg.obs.cn-north-1.myhwclouds.com/bgm_recognition";

$token = gettoken($username, $password, $domainName, $regionName);

$result = moderation_video($token, $demo_data_url, 5, array("terrorism", "porn", "politics"));
echo json_encode($result);
echo "\n";
/**
 * ak,sk 方式请求
 */

$app_key = "*************";
$app_secret = "*************";

// obs的url方式请求
$result = moderation_video_aksk($app_key, $app_secret, $demo_data_url, 5, array("terrorism", "porn", "politics"));
echo json_encode($result);


