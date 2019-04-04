<?php
/**
 * 低光照增强token 方式请求的服务示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/dark_enhance.php";
require "./ais_sdk/utils.php";

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名
initRegion($region = "cn-north-1");

$filepath = "./data/dark-enhance-demo.bmp";
$image = fileToBase64($filepath);

$token = gettoken($username, $password, $domainName);

$result = dark_enhance($token, $image, 0.9);
print_r($result);
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"];
base64ToFile("data/dark-enhance-demo-token.bmp", $basestr);

