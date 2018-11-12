<?php
/**
 * 图片内容检测服务的示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/image_content.php";
require "./ais_sdk/utils.php";

$filepath = "./data/moderation-terrorism.jpg";
$data = fileToBase64($filepath);

$data_url = "https://ais-sample-data.obs.cn-north-1.myhwclouds.com/terrorism.jpg";
/**
 * token 方式请求
 */
$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名
$regionName = "********";    // 配置区域信息

$token = gettoken($username, $password, $domainName, $regionName);

// 图片base64方式请求接口
$result = image_content($token, $data, "", array("politics"), "");
echo $result;

// 图片的obs 的url方式请求接口
$result = image_content($token, "", $data_url, array("politics"), "");
echo $result;

/**
 * ak,sk 方式请求
 */

$app_key = "*************";
$app_secret = "*************";

// 图片的base64 的方式请求接口
$result = image_content_aksk($app_key, $app_secret, $data, "", array("politics"), "");
echo $result;

// 图片的osb的url 方式请求接口
$result = image_content_aksk($app_key, $app_secret, "", $data_url, array("politics"), "");
echo $result;
