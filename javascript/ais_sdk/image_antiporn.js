var https = require("https");
var utils = require("./utils");
var signer = require("./signer");
var ais = require("./ais");

module.exports = {
    image_antiporn: function (token, data, url, callback) {

        var endPoint = utils.getEndPoint(ais.MODERATION_SERVICE);

        // 构建请求信息和请求参数信息
        var requestData = {"image": data, "url": url};
        var requestBody = JSON.stringify(requestData);

        var host = endPoint;
        var method = "POST";
        var uri = ais.IMAGE_ANTI_PORN;
        var headers = {"Content-Type": "application/json", "X-Auth-Token": token, "Content-Length": requestBody.length};
        var options = utils.getHttpRequestEntityOptions(host, method, uri, headers);

        var request = https.request(options, function (response) {

            // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
            if (response.statusCode !== 200) {
                console.log('Http status code is: ' + response.statusCode);
            }
            // 处理结果信息，输入返回信息
            response.on("data", function (chunk) {
                callback(chunk.toString())
            })
        });

        request.on("error", function (err) {
            console.log(err.message);
        });

        request.write(requestBody);
        request.end();
    },

    image_antiporn_aksk: function (_ak, _sk, data, url, callback) {

        // 配置ak，sk信息
        var sig = new signer.Signer();
        sig.AppKey = _ak;                   // 构建ak
        sig.AppSecret = _sk;                // 构建sk

        var endPoint = utils.getEndPoint(ais.MODERATION_SERVICE);

        // 构建请求信息和请求参数信息
        var requestData = {"image": data, "url": url};
        var _headers = {"Content-Type": "application/json"};
        var req = new signer.HttpRequest();
        var options = utils.getHttpRequestEntity(sig, req, endPoint, "POST", ais.IMAGE_ANTI_PORN, "", _headers, requestData);

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
            console.log(err.message);
        });

        requset.write(req.body);
        requset.end();
    }

};