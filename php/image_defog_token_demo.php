<?php
/**
 * 图像去雾服务token 方式请求的示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/image_defog.php";
require "./ais_sdk/utils.php";

// 当前支持北京1：cn-north-1 和香港：ap-southeast-1 等region信息
init_region($region = 'cn-north-1');
$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名

$filepath = "./data/defog-demo.png";
$image = file_to_base64($filepath);

$token = get_token($username, $password, $domainName);

$result = image_defog($token, $image, 1.5, true);
print_r($result);
$resultobj = json_decode($result, true);
$basestr = $resultobj["result"];
base64_to_file("data/defog-demo-token.png", $basestr);
