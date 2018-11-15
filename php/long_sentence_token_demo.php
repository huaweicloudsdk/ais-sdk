<?php
/**
 * 语音识别服务token 方式请求的使用示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/long_sentence.php";
require "./ais_sdk/utils.php";

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名
$regionName = "********";    // 配置区域信息

$filepath = "./data/asr-sentence.wav";
$data = fileToBase64($filepath);

$data_url = "https://ais-sample-data.obs.myhwclouds.com/lsr-1.mp3";

$token = gettoken($username, $password, $domainName, $regionName);

// obs的url方式请求
$result = long_sentence($token, "", $data_url);
echo $result;
echo "\n";

// base64 方式请求
$result = long_sentence($token, $data, "");
echo $result;

