<?php
require "signer.php";
require "ais.php";

/**
 * token 方式
 * @return stri
 */
function long_sentence($token, $data, $url = "", $category = "common")
{
    // 获取任务信息
    $jobResult = _long_sentence($token, $data, $url, $category);
    $jobResultObj = json_decode($jobResult, true);
    $job_id = $jobResultObj['result']['job_id'];
    echo "Process job id is :" . $job_id;

    $words = "";
    while (true) {

        // 获取任务解析的结果
        $resultobj = get_result($token, $job_id);
        if ($resultobj['status'] != 200) {
            var_dump($resultobj);
        }

        if ($resultobj['result']['status_code'] == -1) {

            var_dump($resultobj);

        } // 任务处理完毕
        elseif ($resultobj['result']['status_code'] == 2) {

            $words = $resultobj['result']['words'];
            break;
        } // 任务未完成处理，轮询请求接口处理
        else {
            sleep(2);
            continue;
        }

    }
    return $words;

}


function _long_sentence($token, $data, $url = "", $category = "common")
{
    // 构建请求信息
    $_url = "https://" . ENDPOINT . LONG_SENTENCE;

    $data = array(
        "data" => $data,                        // data: 音频文件的base64
        "url" => $url,                          // url: 音频文件的obs的url地址
        "category" => $category,                // category：语音交流方式
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

        // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
        if ($status == 200) {
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
function get_result($token, $job_id)
{
    $url = "https://" . ENDPOINT . LONG_SENTENCE . "?job_id=" . $job_id;

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

        // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
        if ($status == 200) {

            $response = json_decode($response, true);
            $response['status'] = $status;
            return $response;
        } else {
            echo "The http status code for get result request failure: " . $status . "\n";
            echo $response;
        }

    }
    curl_close($curl);
}

/**
 * ak,sk 方式
 */
function long_sentence_aksk($_ak, $_sk, $data, $url = "", $category = "common")
{
    // 构建ak，sk对象
    $signer = new Signer();
    $signer->AppKey = $_ak;             // 构建ak
    $signer->AppSecret = $_sk;          // 构建sk

    $jobResult = _long_sentence_aksk($signer, $data, $url, $category);
    $jobResultObj = json_decode($jobResult, true);
    $job_id = $jobResultObj['result']['job_id'];
    echo "Process job id is :" . $job_id;

    $words = "";
    while (true) {

        // 获取任务的执行结果
        $resultobj = get_result_aksk($signer, $job_id);

        if ($resultobj['status'] != 200) {
            var_dump($resultobj);
        }

        if ($resultobj['result']['status_code'] == -1) {
            var_dump($resultobj);

            // 任务处理成功，返回结果信息
        } elseif ($resultobj['result']['status_code'] == 2) {
            $words = $resultobj['result']['words'];
            break;
        } // 任务处理未完成，轮询继续请求接口
        else {
            sleep(5);
            continue;
        }

    }
    return $words;
}

/**
 * 获取任务信息
 */
function _long_sentence_aksk($signer, $data, $url = "", $category = "common")
{
    // 构建请求信息
    $data = array(
        "data" => $data,                        // data: 音频文件的base64
        "url" => $url,                          // url: 音频文件的obs的url地址
        "category" => $category,                // category：语音交流方式
    );

    $req = new Request();
    $req->method = 'POST';
    $req->scheme = 'https';
    $req->host = ENDPOINT;
    $req->uri = LONG_SENTENCE;
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

        // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
        if ($status == 200) {
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
function get_result_aksk($signer, $job_id)
{

    $req = new Request();
    $req->method = 'GET';
    $req->scheme = 'https';
    $req->host = ENDPOINT;
    $req->uri = LONG_SENTENCE;
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

        // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
        if ($status == 200) {

            $response = json_decode($response, true);
            $response['status'] = $status;
            return $response;
        } else {
            echo "The http status code for get result request failure: " . $status . "\n";
            echo $response;
        }

    }
    curl_close($curl);
}