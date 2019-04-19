<?php
/**
 * 名人识别检测服务的token 方式请求示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/celebrity_recognition.php";
require "./ais_sdk/utils.php";

// region目前支持华北-北京一(cn-north-1)、亚太-香港(ap-southeast-1)
init_region($region = 'cn-north-1');

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名

$filepath = "./data/celebrity-recognition.jpg";
$data = file_to_base64($filepath);

// obs链接需要和region区域一致，不同的region的obs资源不共享
$data_url = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/celebrity-recognition.jpg";

$token = get_token($username, $password, $domainName);

// 图片base64方式请求接口
$result = celebrity_recognition($token, $data, "");
echo $result;

// 图片的obs 的url方式请求接口
$result = celebrity_recognition($token, "", $data_url);
echo $result;

