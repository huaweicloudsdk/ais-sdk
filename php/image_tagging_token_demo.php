<?php
/**
 * 图片标签内容服务token请求方式的示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/image_tagging.php";
require "./ais_sdk/utils.php";

// region目前支持华北-北京一(cn-north-1)、亚太-香港(ap-southeast-1)
init_region($region = 'cn-north-1');

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名

$filepath = "./data/image-tagging-demo.jpg";
$data = file_to_base64($filepath);

// obs链接需要和region区域一致，不同的region的obs资源不共享
$data_url = "https://ais-sample-data.obs.myhuaweicloud.com/tagging-normal.jpg";

$token = get_token($username, $password, $domainName);

// 图片base64方式请求接口
$result = image_tagging($token, $data, "", 5, "en", 2);
echo $result;

// 图片的obs 的url方式请求接口
$result = image_tagging($token, "", $data_url, 5, "en", 2);
echo $result;