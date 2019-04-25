<?php
require "signer.php";
require "ais.php";

/**
 * token 方式
 * @return stri
 */
function moderation_video($token, $url, $frame_interval, $category)
{
    $endPoint = get_endpoint(MODERATION);
    // 获取任务信息
    $jobResult = _moderation_video($endPoint, $token, $url, $frame_interval, $category);
    $jobResultObj = json_decode($jobResult, true);
    $job_id = $jobResultObj['result']['job_id'];
    echo "Process job id is :" . $job_id;

    $retryTimes = 0;
    while (true) {

        // 获取任务解析的结果
        $resultobj = get_result($endPoint, $token, $job_id);
        if (!status_success($resultobj['status'])) {
            // 如果查询次数小于最大次数，进行重试
            if($retryTimes < RETRY_MAX_TIMES){
                $retryTimes++;
                sleep(2);
                continue;
            }else{
                var_dump($resultobj);
            }
        }
        // 任务处理失败，直接退出
        if ($resultobj['result']['status'] == "failed") {
            var_dump($resultobj);

        } // 任务处理完毕
        elseif ($resultobj['result']['status'] == "finish") {
            return $resultobj;
        } // 任务未完成处理，轮询请求接口处理
        else {
            sleep(2);
            continue;
        }

    }

}


function _moderation_video($endPoint, $token, $url, $frame_interval, $category)
{
    // 构建请求信息
    $_url = "https://" . $endPoint . MODERATION_VIDEO;

    $data = array(
        "url" => $url,                          // url：视频的URL路径
        "frame_interval" => $frame_interval,    // frame_interval：非必选 截帧时间间隔。number
        "category" => $category,                // categories：非必选 检测场景 array politics：是否涉及政治人物的检测。terrorism：是否包含暴恐元素的检测。porn：是否包含涉黄内容元素的检测
    );

    $curl = curl_init();
    $headers = array(
        "Content-Type:application/json",
        "X-Auth-Token:" . $token);

    // 请求信息封装
    curl_setopt($curl, CURLOPT_URL, $_url);
    curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
    curl_setopt($curl, CURLOPT_POSTFIELDS, json_encode($data));
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, TRUE);
    curl_setopt($curl, CURLOPT_NOBODY, FALSE);
    curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);

    // 执行请求信息
    $response = curl_exec($curl);
    $status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
    if ($status == 0) {
        echo curl_error($curl);
    } else {

        // 验证服务调用返回的状态是否成功，如果为2xx, 为成功, 否则失败。
        if (status_success($status)) {
            return $response;
        } else {
            echo "The http status code for get jobId request failure: " . $status . "\n";
            echo $response;
        }

    }
    curl_close($curl);
}

/**
 * 获取结果值
 * @param $token
 * @param $job_id
 */
function get_result($endPoint, $token, $job_id)
{
    $url = "https://" . $endPoint . MODERATION_VIDEO . "?job_id=" . $job_id;

    $headers = array(
        "Content-Type:application/json",
        "X-Auth-Token:" . $token);

    $curl = curl_init(); // 启动一个CURL会话
    curl_setopt($curl, CURLOPT_URL, $url);
    curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($curl, CURLOPT_HEADER, 0);
    curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);

    // 执行请求信息
    $response = curl_exec($curl);
    $status = curl_getinfo($curl, CURLINFO_HTTP_CODE);

    if ($status == 0) {
        echo curl_error($curl);
    } else {
            $response = json_decode($response, true);
            $response['status'] = $status;
            return $response;
    }
    curl_close($curl);
}

/**
 * ak,sk 方式
 */
function moderation_video_aksk($_ak, $_sk, $url, $frame_interval, $category)
{
    // 构建ak，sk对象
    $signer = new Signer();
    $signer->AppKey = $_ak;             // 构建ak
    $signer->AppSecret = $_sk;          // 构建sk

    $endPoint = get_endpoint(MODERATION);

    $jobResult = _moderation_video_aksk($endPoint, $signer, $url, $frame_interval, $category);
    $jobResultObj = json_decode($jobResult, true);
    $job_id = $jobResultObj['result']['job_id'];
    echo "Process job id is :" . $job_id;

    $retryTimes = 0;
    while (true) {

        // 获取任务的执行结果
        $resultobj = get_result_aksk($endPoint, $signer, $job_id);

        if (!status_success($resultobj['status'])) {
            // 如果查询次数小于最大次数，进行重试
            if($retryTimes < RETRY_MAX_TIMES){
                $retryTimes++;
                sleep(2);
                continue;
            }else{
                var_dump($resultobj);
            }

        }

        if ($resultobj['result']['status'] == "failed") {
            var_dump($resultobj);

            // 任务处理成功，返回结果信息
        } elseif ($resultobj['result']['status'] == "finish") {
            return $resultobj;
        } // 任务处理未完成，轮询继续请求接口
        else {
            sleep(5);
            continue;
        }

    }
}

/**
 * 获取任务信息
 */
function _moderation_video_aksk($endPoint, $signer, $url, $frame_interval, $category = "common")
{
    // 构建请求信息
    $data = array(
        "url" => $url,                          // url：视频的URL路径
        "frame_interval" => $frame_interval,    // frame_interval：非必选 截帧时间间隔。number
        "category" => $category,                // categories：非必选 检测场景 array politics：是否涉及政治人物的检测。terrorism：是否包含暴恐元素的检测。porn：是否包含涉黄内容元素的检测
    );

    $req = new Request();
    $req->method = 'POST';
    $req->scheme = 'https';
    $req->host = $endPoint;
    $req->uri = MODERATION_VIDEO;
    $req->body = json_encode($data);
    $req->headers = array(
        'Content-Type' => 'application/json'
    );
    $curl = $signer->Sign($req);
    curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);

    // 执行请求信息
    $response = curl_exec($curl);
    $status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
    if ($status == 0) {
        echo curl_error($curl);
    } else {

        // 验证服务调用返回的状态是否成功，如果为2xx, 为成功, 否则失败。
        if (status_success($status)) {
            return $response;
        } else {
            echo "The http status code for get jobId request failure:" . $status . "\n";
            echo $response;
        }

    }
    curl_close($curl);
}


/**
 * 获取结果值
 * @param $token
 * @param $job_id
 */
function get_result_aksk($endPoint, $signer, $job_id)
{

    $req = new Request();
    $req->method = 'GET';
    $req->scheme = 'https';
    $req->host = $endPoint;
    $req->uri = MODERATION_VIDEO;
    $req->query = array(
        'job_id' => $job_id
    );
    $req->headers = array(
        'Content-Type' => 'application/json'
    );
    $curl = $signer->Sign($req);
    curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);

    // 执行请求信息
    $response = curl_exec($curl);
    $status = curl_getinfo($curl, CURLINFO_HTTP_CODE);

    if ($status == 0) {
        echo curl_error($curl);
    } else {

        $response = json_decode($response, true);
        $response['status'] = $status;
        return $response;
    }
    curl_close($curl);
}