<?php
/**
 * 视频背景音乐识别服务的token方式请求示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/asr_bgm.php";
require "./ais_sdk/utils.php";

// region目前支持华北-北京一(cn-north-1)、亚太-香港(ap-southeast-1)
init_region($region = 'cn-north-1');

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名

$token = get_token($username, $password, $domainName);

// obs链接需要和region区域一致，不同的region的obs资源不共享
$data_url = "https://obs-test-llg.obs.cn-north-1.myhuaweicloud.com/bgm_recognition";

$result = asr_bgm($token, $data_url);
echo $result;
$resultobj = json_decode($result, true);
echo $resultobj["result"]["audio_name"];
