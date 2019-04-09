<?php
/**
 * 图片内容检测批量服务token 方式请求的示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/image_moderation_batch.php";
require "./ais_sdk/utils.php";

// 当前支持北京1：cn-north-1 和香港：ap-southeast-1 等region信息
init_region($region = 'cn-north-1');
$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名

$data_url1 = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg";
$data_url2 = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg";

$token = get_token($username, $password, $domainName);

$result = image_content_batch($token, array($data_url1, $data_url2), array("politics", "terrorism", "porn"), 0);
echo $result;