var https = require("https");
var utils = require("./utils");
var signer = require("./signer");
var ais = require("./ais");

module.exports = {
    asr_bgm: function (token, url, callback) {

        var endPoint = utils.getEndPoint(ais.IMAGE_SERVICE);
        // 构建请求信息
        var requestBody = JSON.stringify({"url": url});
        var headers = {
            "Content-Type": "application/json",
            "X-Auth-Token": token,
            "Content-Length": requestBody.length
        };
        var options = utils.getHttpRequestEntityOptions(endPoint, "POST", ais.ASR_BGM, headers, );

        var requset = https.request(options, function (response) {

            // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
            if (response.statusCode !== 200) {
                console.log('Http status code is: ' + response.statusCode);
            }

            response.on("data", function (chunk) {
                // 返回中文unicode处理
                var result = JSON.parse(chunk.toString());
                callback(JSON.stringify(result));
            })
        });

        requset.on("error", function (err) {
            console.log(err.message);
        });
        requset.write(requestBody);
        requset.end();
    },

    asr_bgm_aksk: function (_ak, _sk, _url, callback) {

        var endPoint = utils.getEndPoint(ais.IMAGE_SERVICE);
        // 配置ak，sk信息
        var sig = new signer.Signer();
        sig.AppKey = _ak;    //构建ak
        sig.AppSecret = _sk; //构建sk

        // 构建请求信息和请求参数信息
        var requestData = {"url": _url};
        var _headers = {"Content-Type": "application/json"};
        var req = new signer.HttpRequest();
        var options = utils.getHttpRequestEntity(sig, req, endPoint, "POST", ais.ASR_BGM, "", _headers, requestData);

        var requset = https.request(options, function (response) {

            // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
            if (response.statusCode !== 200) {
                console.log('Http status code is: ' + response.statusCode);
            }
            response.on("data", function (chunk) {
                callback(chunk.toString());
            })
        });

        requset.on("error", function (err) {
            // 返回中文unicode处理
            var result = JSON.parse(chunk.toString());
            callback(JSON.stringify(result));
        });

        requset.write(req.body);
        requset.end();
    }

};