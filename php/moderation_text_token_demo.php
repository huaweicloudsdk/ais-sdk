<?php
/**
 * 文本检测服务token 方式请求的示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/moderation_text.php";
require "./ais_sdk/utils.php";

$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名
$regionName = "********";    // 配置区域信息

$categories = array(
    array(
        "text" => "666666luo聊请+110亚砷酸钾六位qq，fuck666666666666666",
        "type" => "content"
    )
);

$items = array("ad", "politics", "politics", "politics", "contraband", "contraband");

$token = gettoken($username, $password, $domainName, $regionName);

$result = moderation_text($regionName, $token, $categories, $items);
echo $result;

