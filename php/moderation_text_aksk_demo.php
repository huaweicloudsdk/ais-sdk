<?php
/**
 * 文本检测服务的aksk请求方式的示例
 */
require "./ais_sdk/moderation_text.php";
require "./ais_sdk/utils.php";

// 当前支持北京1：cn-north-1 和香港：ap-southeast-1 等region信息
init_region($region = 'cn-north-1');
$app_key = "*********";
$app_secret = "*********";

$categories = array(
    array(
        "text" => "666666luo聊请+110亚砷酸钾六位qq，fuck666666666666666",
        "type" => "content"
    )
);

$items = array("ad", "abuse", "politics", "porn", "contraband");
$result = moderation_text_aksk($app_key, $app_secret, $categories, $items);
echo $result;

