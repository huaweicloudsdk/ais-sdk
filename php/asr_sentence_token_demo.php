<?php
/**
 * 语音识别服务的token 方式请求使用示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/asr_sentence.php";
require "./ais_sdk/utils.php";

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名
$regionName = "********";    // 配置区域信息

$filepath = "./data/asr-sentence.wav";
$data = fileToBase64($filepath);

$data_url = "https://ais-sample-data.obs.myhwclouds.com/asr-sentence.wav";

$token = gettoken($username, $password, $domainName, $regionName);

// base64 方式请求
$result = asr_sentence($token, $data, "", "wav", "16k");
echo $result;
echo "\n";

// obs的url方式请求
$result = asr_sentence($token, "", $data_url, "wav", "16k");
echo $result;


