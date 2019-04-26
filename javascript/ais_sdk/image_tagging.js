var https = require("https");
var utils = require("./utils");
var signer = require("./signer");
var ais = require("./ais");

module.exports = {
    image_tagging: function (token, data, url, threshold, language, limit, callback) {

        var endPoint = utils.getEndPoint(ais.IMAGE_SERVICE);

        /**
         * image: 图片信息
         * url： 与image 二选一
         * language： 可选 返回标签语言
         * limit： 可选 最多的tag数
         * threshold：可选 标签的置信度，低于不返回
         * @type {string}
         */
        var requestData = {"image": data, url: url, "threshold": threshold, "language": language, "limit": limit};
        var requestBody = JSON.stringify(requestData);

        var options = utils.getHttpRequestEntityOptions(endPoint, "POST", ais.IMAGE_TAGGING, {
            "Content-Type": "application/json",
            "X-Auth-Token": token,
            "Content-Length":requestBody.length
        });

        var request = https.request(options, function (response) {

            // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
            if (response.statusCode !== 200) {
                console.log('Http status code is: ' + response.statusCode);
            }

            // 检测服务结果
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

    image_tagging_aksk: function (_ak, _sk, data, url, threshold, language, limit, callback) {

        // 配置ak，sk信息
        var sig = new signer.Signer();
        sig.AppKey = _ak;                   // 构建ak
        sig.AppSecret = _sk;                // 构建sk

        var endPoint = utils.getEndPoint(ais.IMAGE_SERVICE);

        // 构建请求信息和请求参数信息
        /**
         * image: 图片信息
         * urk: 与image 二选一
         * language：可选 返回标签语言
         * limit：可选 最多的tag数
         * threshold：可选 标签的置信度，低于不返回
         * @type {string}
         */
        var requestData = {"image": data, "url": url, "threshold": threshold, "language": language, "limit": limit};
        var req = new signer.HttpRequest();
        var options = utils.getHttpRequestEntity(sig, req, endPoint, "POST", ais.IMAGE_TAGGING, "", {"Content-Type": "application/json"}, requestData);

        var request = https.request(options, function (response) {

            // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
            if (response.statusCode !== 200) {
                console.log('Http status code is: ' + response.statusCode);
            }

            // 返回图像内容检测服务结果信息
            response.on("data", function (chunk) {
                callback(chunk.toString());
            })
        });

        request.on("error", function (err) {
            console.log(err.message);
        });

        request.write(req.body);
        request.end();
    }

};