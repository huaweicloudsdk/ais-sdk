<?php
/**
 * 图片清晰度检测的服务token 方式请求的使用示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/clarity_detect.php";
require "./ais_sdk/utils.php";

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名
$regionName = "********";    // 配置区域信息

$filepath = "./data/moderation-clarity-detect.jpg";
$data = fileToBase64($filepath);

$data_url = "https://ais-sample-data.obs.cn-north-1.myhwclouds.com/vat-invoice.jpg";

$token = gettoken($username, $password, $domainName, $regionName);


// base64 方式请求
$result = clarity_detect($token, $data, "", 0.8);
echo $result;

// obs的url方式请求
$result = clarity_detect($token, "", $data_url, 0.9);
echo $result;


