<?php
/**
 * 图像去雾服务token 方式请求的示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/image_defog.php";
require "./ais_sdk/utils.php";

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名
initRegion($region = "cn-north-1");

$filepath = "./data/defog-demo.png";
$image = fileToBase64($filepath);

$token = gettoken($username, $password, $domainName, $regionName);

$result = image_defog($token, $image, 1.5, true);
print_r($result);
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"];
base64ToFile("data/defog-demo-token.png", $basestr);
