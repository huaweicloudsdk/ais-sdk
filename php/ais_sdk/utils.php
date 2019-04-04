<?php
$regionName = "cn-north-1";

$_endponit = array(
    "image" => array(
       "cn-north-1" => "image.cn-north-1.myhuaweicloud.com",
        "ap-southeast-1" => "image.ap-southeast-1.myhuaweicloud.com"
    ),
    "moderation" => array(
        "cn-north-1" => "moderation.cn-north-1.myhuaweicloud.com",
        "ap-southeast-1" => "moderation.ap-southeast-1.myhuaweicloud.com"
    )
);

/**
 * 文件转化为base64
 * @param $file 文件路径
 * @return string
 */
function fileToBase64($file)
{
    $base64data = "";
    if (file_exists($file)) {
        $base64data = base64_encode(file_get_contents($file));
    }
    return $base64data;
}

/**
 * 将base64位的字符串转成文件
 * @param $filePath
 * @param $base64str
 */
function base64ToFile($filePath, $base64str)
{
    if ($base64str != null) {

        // 将base64的字符串写入文件路径
        $flag = file_put_contents($filePath, base64_decode($base64str));
        if (!$flag) {
            echo "base64str change to file failed";
        } else {
            echo $filePath;
            echo "\n";
        }

    } else {
        echo "base64str is null!";
    }

}

/**
 * 初始化region信息
 * @param $region
 */
function initRegion($region){
    $GLOBALS["regionName"] = $region;
}


/**
 * 获取服务的域名
 * @param $type
 * @return mixed
 */
function getEndpoint($type)
{
    global $_endponit;
    return $_endponit[$type][$GLOBALS["regionName"]];
}
