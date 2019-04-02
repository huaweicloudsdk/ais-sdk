<?php
/**
 * 图片标签内容服务token请求方式的示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/image_tagging.php";
require "./ais_sdk/utils.php";

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名
$regionName = "********";    // 配置区域信息

$filepath = "./data/image-tagging-demo.jpg";
$data = fileToBase64($filepath);

$data_url = "https://ais-sample-data.obs.myhuaweicloud.com/tagging-normal.jpg";

$token = gettoken($username, $password, $domainName, $regionName);

// 图片base64方式请求接口
$result = image_tagging($regionName, $token, $data, "", 5, "en", 2);
echo $result;

// 图片的obs 的url方式请求接口
$result = image_tagging($regionName, $token, "", $data_url, 5, "en", 2);
echo $result;