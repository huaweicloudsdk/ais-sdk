var https = require("https");
var utils = require("./utils");
var signer = require("./signer");
var ais = require("./ais");

function video(token, url, frame_interval, category, callback) {

    var endPoint = utils.getEndPoint(ais.MODERATION_SERVICE);
    /**
     *  url：视频的URL路径
     *  frame_interval：非必选 截帧时间间隔。number
     *  categories：非必选 检测场景 array politics：是否涉及政治人物的检测。terrorism：是否包含暴恐元素的检测。porn：是否包含涉黄内容元素的检测。
     * @type {string}
     */
    var requestData = {"url": url, "frame_interval": frame_interval, "category": category};
    var requestBody = JSON.stringify(requestData);

    // 构建请求信息
    var headers = {"Content-Type": "application/json", "X-Auth-Token": token, "Content-Length": requestBody.length};
    var options = utils.getHttpRequestEntityOptions(endPoint, "POST", ais.MODERATION_VIDEO, headers);

    var request = https.request(options, function (response) {

        response.on("data", function (chunk) {

            // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
            if (response.statusCode !== 200) {
                console.log('Http status code is: ' + response.statusCode);
                console.log(chunk.toString());
                return;
            }

            // 获取job_id
            var result = JSON.parse(chunk);
            job_id = result.result.job_id;
            console.log('Process job id is :' + job_id);
            var retryTimes = 0;

            // 根据job_id的结果,获取视频审核信息
            var results = "";
            get_result(endPoint, job_id, results, token, retryTimes, callback);
        })
    });

    request.on("error", function (err) {
        console.log(err.message);
    });

    request.write(requestBody);
    request.end();
}


function get_result(endPoint, job_id, resultsearch, token, retryTimes, callback) {
    // 构建请求参数和请求信息
    var requestData = {'job_id': job_id};
    var options = utils.getHttpRequestEntityForGet(endPoint, "GET", ais.MODERATION_VIDEO, {
        "Content-Type": "application/json",
        "X-Auth-Token": token
    }, requestData);

    // 轮询请求视频审核接口，获取结果信息
    var reqsearch = https.request(options, function (res) {
        res.setEncoding('utf8');
        res.on('data', function (chunk) {
            resultsearch = JSON.parse(chunk);
            if (res.statusCode !== 200) {
                if(retryTimes < ais.RETRY_TIMES_MAX){
                    retryTimes++;
                    // 满足条件进行重试
                    setTimeout(function () {
                        get_result_aksk(endPoint, sign, job_id, resultsearch, retryTimes, callback);
                    }, 2000);
                    console.log('The processing job request failed, retrying');
                }else {
                    console.log('Http status code is: ' + response.statusCode);
                    console.log(chunk.toString());
                }
                return;
            }

            // 如果处理失败，直接退出
            if (resultsearch !== "" && resultsearch.result.status === "failed") {
                console.log('The processing job request failed');
                callback(JSON.stringify(resultsearch));
                return;
            }

            // 任务处理成功
            if (resultsearch !== "" && resultsearch.result.status === "finish") {
                callback(JSON.stringify(resultsearch));
            }
            else {

                // 如果没有返回，等待一段时间，继续进行轮询。
                setTimeout(function () {
                    get_result(endPoint, job_id, resultsearch, token, retryTimes, callback);
                }, 2000);
            }
        });
    });

    reqsearch.on('error', function (e) {
        console.log('problem with request: ' + e.message);
    });

    reqsearch.write("");
    reqsearch.end();
}

function video_aksk(_ak, _sk, url, frame_interval, category, callback) {
    // 配置ak，sk信息
    var sig = new signer.Signer();
    sig.AppKey = _ak;                     // 构建ak
    sig.AppSecret = _sk;                  // 构建sk

    var endPoint = utils.getEndPoint(ais.MODERATION_SERVICE);

    // 构建请求信息和请求参数信息
    var requestData = {"url": url, "frame_interval": frame_interval, "category": category};
    var job_id = "";
    var req = new signer.HttpRequest();
    var header = {"Content-Type": "application/json"};
    var options = utils.getHttpRequestEntity(sig, req, endPoint, "POST", ais.MODERATION_VIDEO, "", header, requestData);

    var request = https.request(options, function (response) {

        response.on("data", function (chunk) {

            // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
            if (response.statusCode !== 200) {
                console.log('Http status code is: ' + response.statusCode);
                console.log(chunk.toString());
                return;
            }

            // 获取job_id 信息，获取视频审核信息
            var result = JSON.parse(chunk);
            job_id = result.result.job_id;
            console.log('Process job id is :' + job_id);
            var retryTimes = 0;

            var results = "";
            get_result_aksk(endPoint, sig, job_id, results, retryTimes, callback);
        })
    });

    request.on("error", function (err) {
        console.log(err.message);
    });

    request.write(req.body);
    request.end();
}


/**
 * 通过job_id 的信息，获取视频审核结果
 * @param endPoint ：服务域名
 * @param sign : 签名对象
 * @param job_id : 任务的id
 * @param resultsearch ：获取结果信息
 * @param retryTimes ：失败重试次数
 * @param callback 回调函数
 */
function get_result_aksk(endPoint, sign, job_id, resultsearch, retryTimes, callback) {

    // 构建请求信息和参数信息
    var request = new signer.HttpRequest();
    var options = utils.getHttpRequestEntity(sign, request, endPoint, "GET", ais.MODERATION_VIDEO, {'job_id': job_id}, {"Content-Type": "application/json"}, "");

    // 轮询请求视频接口，获取结果信息
    var reqsearch = https.request(options, function (response) {

        response.setEncoding('utf8');
        response.on('data', function (chunk) {
            resultsearch = JSON.parse(chunk);
            if (response.statusCode !== 200) {
                if(retryTimes < ais.RETRY_TIMES_MAX){
                    retryTimes++;
                    // 满足条件进行重试
                    setTimeout(function () {
                        get_result_aksk(endPoint, sign, job_id, resultsearch, retryTimes, callback);
                    }, 2000);
                    console.log('The processing job request failed, retrying');
                }else {
                    console.log('Http status code is: ' + response.statusCode);
                    console.log(chunk.toString());
                }
                return;
            }

            // 如果处理失败，直接退出
            if (resultsearch !== "" && resultsearch.result.status === "failed") {
                console.log('The processing job request failed');
                callback(JSON.stringify(resultsearch));
                return;
            }

            // 任务处理成功
            if (resultsearch !== "" && resultsearch.result.status === "finish") {
                callback(JSON.stringify(resultsearch));
            }
            else {

                // 如果没有返回，等待一段时间，继续进行轮询。
                setTimeout(function () {
                    get_result_aksk(endPoint, sign, job_id, resultsearch, retryTimes, callback);
                }, 2000);
            }
        });
    });

    reqsearch.on('error', function (e) {
        console.log('problem with request: ' + e.message);

    });

    reqsearch.end();
}

module.exports = {
    video,
    video_aksk
};
