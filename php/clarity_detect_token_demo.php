<?php
/**
 * 图片清晰度检测的服务token 方式请求的使用示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/clarity_detect.php";
require "./ais_sdk/utils.php";

// 当前支持北京1：cn-north-1 和香港：ap-southeast-1 等region信息
init_region($region = 'cn-north-1');
$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名

$filepath = "./data/moderation-clarity-detect.jpg";
$data = file_to_base64($filepath);

$data_url = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/vat-invoice.jpg";

$token = get_token($username, $password, $domainName);


// base64 方式请求
$result = clarity_detect($token, $data, "", 0.8);
echo $result;

// obs的url方式请求
$result = clarity_detect($token, "", $data_url, 0.9);
echo $result;


