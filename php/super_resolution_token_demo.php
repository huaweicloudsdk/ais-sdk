<?php
/**
 * 图像超分重建服务token 方式请求的示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/super_resolution.php";
require "./ais_sdk/utils.php";

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名
initRegion($region = "cn-north-1");

$filepath = "./data/super-resolution-demo.png";
$data = fileToBase64($filepath);

$token = gettoken($username, $password, $domainName);

$result = super_resolution($token, $data, 4, "ESPCN");
echo $result;
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"];
base64ToFile("data/super-resolution-token.png", $basestr);