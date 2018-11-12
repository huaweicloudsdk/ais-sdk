<?php
/**
 * 图像翻拍识别服务的示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/recapture_detect.php";
require "./ais_sdk/utils.php";

$filepath = "./data/recapture-detect-demo.jpg";
$data = fileToBase64($filepath);

$data_url = "https://ais-sample-data.obs.myhwclouds.com/recapture-detect.jpg";
/**
 * token 方式请求
 */
$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名
$regionName = "********";    // 配置区域信息

$token = gettoken($username, $password, $domainName, $regionName);

// 图片base64方式请求接口
$result = recapture_detect($token, $data, "", 0.99, array("recapture"));
echo $result;

// 图片的obs 的url方式请求接口
$result = recapture_detect($token, "", $data_url, 0.99, array("recapture"));
echo $result;

/**
 * ak,sk 方式请求
 */

$app_key = "*************";
$app_secret = "*************";

// 图片的base64 的方式请求接口
$result = recapture_detect_aksk($app_key, $app_secret, $data, "", 0.99, array("recapture"));
echo $result;

// 图片的osb的url 方式请求接口
$result = recapture_detect_aksk($app_key, $app_secret, "", $data_url, 0.99, array("recapture"));
echo $result;
