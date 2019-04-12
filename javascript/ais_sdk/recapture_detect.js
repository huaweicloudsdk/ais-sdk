var https = require("https");
var utils = require("./utils");
var signer = require("./signer");
var ais = require("./ais");

module.exports = {
    recapture_detect: function (token, data, url, threshold, scene, callback) {

        var endPoint = utils.getEndPoint(ais.IMAGE_SERVICE);

        // 构建请求信息和请求参数信息
        var requestData = {"image": data, "url": url, "threshold": threshold, "scene": scene};
        var requestBody = JSON.stringify(requestData);

        var options = utils.getHttpRequestEntityOptions(endPoint, "POST", ais.RECAPTURE_DETECT, {
            "Content-Type": "application/json",
            "X-Auth-Token": token,
            "Content-Length": requestBody.length
        });

        var request = https.request(options, function (response) {

            // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
            if (response.statusCode !== 200) {
                console.log('Http status code is: ' + response.statusCode);
            }

            response.on("data", function (chunk) {
                callback(chunk.toString());
            })
        });

        request.on("error", function (err) {
            console.log(err.message);
        });

        request.write(requestBody);
        request.end();
    },

    recapture_detect_aksk: function (_ak, _sk, data, url, threshold, scene, callback) {

        // 配置ak，sk信息
        var sig = new signer.Signer();
        sig.AppKey = _ak;                   // 构建ak
        sig.AppSecret = _sk;                // 构建sk

        var endPoint = utils.getEndPoint(ais.IMAGE_SERVICE);

        var requestData = {"image": data, "url": url, "threshold": threshold, "scene": scene};
        var req = new signer.HttpRequest();
        var options = utils.getHttpRequestEntity(sig, req, endPoint, "POST", ais.RECAPTURE_DETECT, "", {"Content-Type": "application/json"}, requestData);

        var request = https.request(options, function (response) {

            // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
            if (response.statusCode !== 200) {
                console.log('Http status code is: ' + response.statusCode);
            }

            response.on("data", function (chunk) {
                console.log(chunk.toString());
            })
        });

        request.on("error", function (err) {
            callback(err.message);
        });

        request.write(req.body);
        request.end();
    }
};