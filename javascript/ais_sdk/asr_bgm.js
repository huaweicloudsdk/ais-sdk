var https = require("https");
var utils = require("./utils");
var signer = require("./signer");

module.exports = {
    asr_bgm: function (token, url, callback) {

        // 构建请求信息
        var host = "ais.cn-north-1.myhuaweicloud.com";
        var uri = "/v1.0/bgm/recognition";
        var headers = {
            "Content-Type": "application/json",
            "X-Auth-Token": token
        };
        var options = utils.getHttpRequestEntityOptions(host, "POST", uri, headers);
        var requestBody = JSON.stringify({"url": url});

        var requset = https.request(options, function (response) {

            // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
            if (response.statusCode !== 200) {
                console.log("Process the background music result failed!");
                return;
            }

            response.on("data", function (chunk) {
                callback(chunk.toString());
            })
        });

        requset.on("error", function (err) {
            console.log(err.message);
        });
        requset.write(requestBody);
        requset.end();
    },

    asr_bgm_aksk: function (_ak, _sk, _url, callback) {

        // 配置ak，sk信息
        var sig = new signer.Signer();
        sig.AppKey = _ak;    //构建ak
        sig.AppSecret = _sk; //构建sk

        // 构建请求信息和请求参数信息
        var requestData = {"url": _url};
        var host = "ais.cn-north-1.myhuaweicloud.com";
        var _headers = {"Content-Type": "application/json"};
        var req = new signer.HttpRequest();
        var options = utils.getHttpRequestEntity(sig, req, host, "POST", "/v1.0/bgm/recognition", "", _headers, requestData);

        var requset = https.request(options, function (response) {

            // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
            if (response.statusCode !== 200) {
                console.log("Process the background music result failed!");
                return;
            }
            response.on("data", function (chunk) {
                callback(chunk.toString());
            })
        });

        requset.on("error", function (err) {
            console.log(err.message);
        });

        requset.write(req.body);
        requset.end();
    }

};