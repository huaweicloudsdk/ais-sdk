<?php
/**
 * 文本检测服务token 方式请求的示例
 */
require "./ais_sdk/gettoken.php";
require "./ais_sdk/moderation_text.php";
require "./ais_sdk/utils.php";

// 当前支持北京1：cn-north-1 和香港：ap-southeast-1 等region信息
init_region($region = 'cn-north-1');
$username = "********";      // 配置用户名
$password = "********";      // 密码
$domainName = "*********";   // 配置用户名

$categories = array(
    array(
        "text" => "666666luo聊请+110亚砷酸钾六位qq，fuck666666666666666",
        "type" => "content"
    )
);

$items = array("ad", "politics", "politics", "politics", "contraband", "contraband");

$token = get_token($username, $password, $domainName);

$result = moderation_text($token, $categories, $items);
echo $result;

