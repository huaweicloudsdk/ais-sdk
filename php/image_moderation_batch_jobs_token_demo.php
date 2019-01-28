<?php
/**
 * 图片内容检测服务token 方式请求的示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/image_moderation_batch_jobs.php";
require "./ais_sdk/utils.php";

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名
$regionName = "********";    // 配置区域信息

$data_url1 = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg";
$data_url2 = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg";

$token = gettoken($username, $password, $domainName, $regionName);

$result = batch_jobs($token, array($data_url1,$data_url2), array("politics", "terrorism", "porn"));
echo json_encode($result);