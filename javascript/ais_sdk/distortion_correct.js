var https = require("https");
var utils = require("./utils");
var signer = require("./signer");
var ais = require("./ais");

module.exports = {
    distortion_correct: function (token, data, url, correction, callback) {

        /**
         * image：与url二选一 图片文件Base64编码字符串
         * url：与image二选一 图片的URL路径，目前支持华为云上OBS提供的临时授权访问的URL，以及匿名公开授权的URL
         * correction：可选 是否要进行图片扭曲校正 boolean true：校正。默认校正。false：不进行校正
         * @type {{image: (*|string), correction: boolean}}
         */
        var requestData = {"image": data, "url": url, "correction": correction};
        var options = utils.getHttpRequestEntityOptions(ais.ENDPOINT, "POST", ais.DISTORTION_CORRECT, {
            "Content-Type": "application/json",
            "X-Auth-Token": token
        });

        var requestBody = JSON.stringify(requestData);

        var request = https.request(options, function (response) {

            // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
            if (response.statusCode !== 200) {
                console.log("Process the distortion correct result failed!");
                return;
            }

            // 拼接返回结果的base64的字符串
            var resultStr = "";
            response.on("data", function (chunk) {
                resultStr += chunk.toString();
            });

            response.on("end", function () {
                callback(resultStr)
            })
        });

        request.on("error", function (err) {
            console.log(err.message);
        });

        request.write(requestBody);
        request.end();
    },

    distortion_correct_aksk: function (_ak, _sk, data, url, correction, callback) {

        // 配置ak，sk信息
        var sig = new signer.Signer();
        sig.AppKey = _ak;                       // 构建ak
        sig.AppSecret = _sk;                    // 构建sk

        /**
         * image：与url二选一 图片文件Base64编码字符串
         * url：与image二选一 图片的URL路径，目前支持华为云上OBS提供的临时授权访问的URL，以及匿名公开授权的URL
         * correction：可选 是否要进行图片扭曲校正 boolean true：校正。默认校正。false：不进行校正
         * @type {{image: (*|string), correction: boolean}}
         */
        var requestData = {"image": data, "url": url, "correction": correction};
        var req = new signer.HttpRequest();
        var options = utils.getHttpRequestEntity(sig, req, ais.ENDPOINT, "POST", ais.DISTORTION_CORRECT, "", {"Content-Type": "application/json"}, requestData);

        var request = https.request(options, function (response) {

            // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
            if (response.statusCode !== 200) {
                console.log("Process the distortion correct result failed!");
                return;
            }

            var resultStr = "";

            // 拼接返回结果的base64的字符串
            response.on("data", function (chunk) {
                resultStr += chunk.toString();
            });

            response.on("end", function () {
                callback(resultStr)
            })
        });

        request.on("error", function (err) {
            console.log(err.message);
        });

        request.write(req.body);
        request.end();
    }
};