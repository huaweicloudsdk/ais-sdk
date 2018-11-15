<?php
/**
 * 语音识别服务token 方式请求的使用示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/moderation_video.php";
require "./ais_sdk/utils.php";

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名
$regionName = "********";    // 配置区域信息

$demo_data_url = "https://obs-test-llg.obs.cn-north-1.myhwclouds.com/bgm_recognition";

$token = gettoken($username, $password, $domainName, $regionName);

$result = moderation_video($token, $demo_data_url, 5, array("terrorism", "porn", "politics"));
echo json_encode($result);

