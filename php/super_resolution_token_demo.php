<?php
/**
 * 图像超分重建服务token 方式请求的示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/super_resolution.php";
require "./ais_sdk/utils.php";

// 当前支持北京1：cn-north-1 和香港：ap-southeast-1 等region信息
init_region($region = 'cn-north-1');
$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名

$filepath = "./data/super-resolution-demo.png";
$data = file_to_base64($filepath);

$token = get_token($username, $password, $domainName);

$result = super_resolution($token, $data, 4, "ESPCN");
echo $result;
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"];
base64_to_file("data/super-resolution-token.png", $basestr);