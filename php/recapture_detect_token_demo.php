<?php
/**
 * 图像翻拍识别服务token 方式请求的示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/recapture_detect.php";
require "./ais_sdk/utils.php";

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名
$regionName = "********";    // 配置区域信息

$filepath = "./data/recapture-detect-demo.jpg";
$data = fileToBase64($filepath);

$data_url = "https://ais-sample-data.obs.myhuaweicloud.com/recapture-detect.jpg";

$token = gettoken($username, $password, $domainName, $regionName);

// 图片base64方式请求接口
$result = recapture_detect($regionName, $token, $data, "", 0.99, array("recapture"));
echo $result;

// 图片的obs 的url方式请求接口
$result = recapture_detect($regionName, $token, "", $data_url, 0.99, array("recapture"));
echo $result;
