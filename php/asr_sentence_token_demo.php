<?php
/**
 * 语音识别服务的token 方式请求使用示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/asr_sentence.php";
require "./ais_sdk/utils.php";

// region目前支持华北-北京一(cn-north-1)
init_region($region = 'cn-north-1');

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名

$filepath = "./data/asr-sentence.wav";
$data = file_to_base64($filepath);

$data_url = "https://obs-ch-sdk-sample.obs.cn-north-1.myhuaweicloud.com/asr-sentence.wav";

$token = get_token($username, $password, $domainName);

// base64 方式请求
$result = asr_sentence($token, $data, "", "wav", "16k");
echo $result;
echo "\n";

// obs的url方式请求
$result = asr_sentence($token, "", $data_url, "wav", "16k");
echo $result;


