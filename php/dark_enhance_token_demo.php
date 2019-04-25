<?php
/**
 * 低光照增强token 方式请求的服务示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/dark_enhance.php";
require "./ais_sdk/utils.php";

// region目前支持华北-北京一(cn-north-1)、亚太-香港(ap-southeast-1)
init_region($region = 'cn-north-1');

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名

$filepath = "./data/dark-enhance-demo.bmp";
$image = file_to_base64($filepath);

$token = get_token($username, $password, $domainName);

$result = dark_enhance($token, $image, 0.9);
print_r($result);
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"];
base64_to_file("data/dark-enhance-demo-token.bmp", $basestr);

