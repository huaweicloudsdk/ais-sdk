var https = require("https");
var utils = require("./utils");
var signer = require("./signer");
var ais = require("./ais");

function long_sentence(token, data, url, category, callback) {
    // 封装获取job_id请求的参数信息
    var requestData = {"data": data, "url": url, "category": category};

    // 构建请求信息
    var headers = {"Content-Type": "application/json", "X-Auth-Token": token};
    var options = utils.getHttpRequestEntityOptions(ais.ENDPOINT, "POST", ais.LONG_SENTENCE, headers);

    var request = https.request(options, function (response) {

        // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
        if (response.statusCode !== 200) {
            console.log("Process the audio result failed!");
            return;
        }
        response.on("data", function (chunk) {
            console.log(chunk.toString());

            // 获取job_id
            var result = JSON.parse(chunk);
            job_id = result.result.job_id;

            // 根据job_id的结果,获取语音识别的信息获取语音识获取语音识别的信息
            var words = "";
            var results = "";
            get_result(job_id, results, words, token, callback);
        })
    });

    request.on("error", function (err) {
        console.log(err.message);
    });

    request.write(JSON.stringify(requestData));
    request.end();
}

function get_result(job_id, resultsearch, words, token, callback) {
    // 构建请求参数和请求信息
    var requestData = {'job_id': job_id};
    var options = utils.getHttpRequestEntityForGet(ais.ENDPOINT, "GET", ais.LONG_SENTENCE, {
        "Content-Type": "application/json",
        "X-Auth-Token": token
    }, requestData);

    // 轮询请求长语音接口，获取结果信息
    var reqsearch = https.request(options, function (res) {
        res.setEncoding('utf8');
        res.on('data', function (chunk) {
            resultsearch = JSON.parse(chunk);

            // 如果处理失败，直接退出
            if (resultsearch !== "" && resultsearch.result.status_code === -1) {
                console.log("Process the audio failed!");
                return;
            }

            // 任务处理成功
            if (resultsearch !== "" && resultsearch.result.status_code === 2) {
                words = resultsearch.result.words;
            }
            // status == 0 || status == 1
            else {

                // 如果没有返回，等待一段时间，继续进行轮询。
                setTimeout(function () {
                    get_result(job_id, resultsearch, words, token, callback);
                }, 2000);
            }

            callback(words);
        });
    });

    reqsearch.on('error', function (e) {
        console.log('problem with request: ' + e.message);
    });

    reqsearch.write("");
    reqsearch.end();
}

function long_sentence_aksk(_ak, _sk, data, url, category, callback) {
    // 配置ak，sk信息
    var sig = new signer.Signer();
    sig.AppKey = _ak;                   // 构建ak
    sig.AppSecret = _sk;                // 构建sk

    // 构建请求信息和请求参数信息
    var requestData = {"data": data, "url": url, "category": category};
    var job_id = "";
    var req = new signer.HttpRequest();
    var header = {"Content-Type": "application/json"};
    var options = utils.getHttpRequestEntity(sig, req, ais.ENDPOINT, "POST", ais.LONG_SENTENCE, "", header, requestData);

    var request = https.request(options, function (response) {

        // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
        if (response.statusCode !== 200) {
            console.log("Process the audio result failed!");
            return;
        }

        response.on("data", function (chunk) {
            console.log(chunk.toString());

            // 获取job_id 信息，获取长语音识别信息
            var result = JSON.parse(chunk);
            job_id = result.result.job_id;
            var words = "";
            var results = "";
            get_result_aksk(sig, job_id, results, words, callback);
        })
    });

    request.on("error", function (err) {
        console.log(err.message);
    });

    request.write(req.body);
    request.end();
}


/**
 * 通过job_id 的信息，获取长语音识别结果
 * @param sign : 签名对象
 * @param job_id : 任务的id
 * @param resultsearch ：获取结果信息
 * @param words 识别结果
 */
function get_result_aksk(sign, job_id, resultsearch, words, callback) {

    // 构建请求信息和参数信息
    var request = new signer.HttpRequest();
    var options = utils.getHttpRequestEntity(sign, request, ais.ENDPOINT, "GET", ais.LONG_SENTENCE, {'job_id': job_id}, {"Content-Type": "application/json"}, "");

    // 轮询请求长语音接口，获取结果信息
    var reqsearch = https.request(options, function (response) {

        response.setEncoding('utf8');
        response.on('data', function (chunk) {
            resultsearch = JSON.parse(chunk);

            //  如果处理失败，直接退出
            if (resultsearch !== "" && resultsearch.result.status_code === -1) {
                console.log("Process the audio failed!");
                return;
            }

            // 任务处理成功
            if (resultsearch !== "" && resultsearch.result.status_code === 2) {
                words = resultsearch.result.words;
            }
            // status == 0 || status == 1
            else {

                // 如果没有返回，等待一段时间，继续进行轮询。
                setTimeout(function () {
                    get_result_aksk(sign, job_id, resultsearch, words, callback);
                }, 2000);
            }
            callback(words)
        });
    });


    reqsearch.on('error', function (e) {
        console.log('problem with request: ' + e.message);

    });

    reqsearch.end();
}

module.exports = {
    long_sentence,
    long_sentence_aksk
};
