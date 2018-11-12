<?php

/**
 * 文件转化为base64
 * @param $file 文件路径
 * @return string
 */
function fileToBase64($file)
{
    $base64data = "";
    if (file_exists($file)) {
        $finfo = finfo_open(FILEINFO_MIME_TYPE);
        finfo_close($finfo);
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