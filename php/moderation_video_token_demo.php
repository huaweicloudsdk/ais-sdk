<?php
/**
 * 语音识别服务token 方式请求的使用示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/moderation_video.php";
require "./ais_sdk/utils.php";

// 当前支持北京1：cn-north-1 和香港：ap-southeast-1 等region信息
init_region($region = 'cn-north-1');
$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名

$demo_data_url = "https://obs-test-llg.obs.cn-north-1.myhuaweicloud.com/bgm_recognition";

$token = get_token($username, $password, $domainName);

$result = moderation_video($token, $demo_data_url, 5, array("terrorism", "porn", "politics"));
echo json_encode($result);

