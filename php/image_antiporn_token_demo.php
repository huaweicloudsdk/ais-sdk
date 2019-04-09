<?php
/**
 * 图片反黄检测服务token 方式请求的示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/image_antiporn.php";
require "./ais_sdk/utils.php";

// 当前支持北京1：cn-north-1 和香港：ap-southeast-1 等region信息
init_region($region = 'cn-north-1');
$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名

$filepath = "./data/moderation-antiporn.jpg";
$data = file_to_base64($filepath);

$data_url = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg";

$token = get_token($username, $password, $domainName);

// 图片base64方式请求接口
$result = image_antiporn($token, $data, "");
echo $result;

// 图片的obs 的url方式请求接口
$result = image_antiporn($token, "", $data_url);
echo $result;

