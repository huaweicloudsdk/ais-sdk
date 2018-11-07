var https = require("https");
var utils = require("./utils");
var signer = require("./signer");

module.exports = {
    defog: function (token, data, gamma, natural_look, callback) {

        // 构建请求信息和请求参数信息
        var host = "ais.cn-north-1.myhwclouds.com";
        var uri = "/v1.0/vision/defog";
        /**
         *  构建请求信息
         * image: 图片信息 base64
         * file：与image 二选一 图片文件
         * gamma：gamma 矫正值
         * natural_look：可选 ，是否保持自然感官
         * @type {string}
         */
        var requestData = {"image": data, "file": "", "gamma": gamma, "natural_look": natural_look};
        var options = utils.getHttpRequestEntityOptions(host, "POST", uri, {
            "Content-Type": "application/json",
            "X-Auth-Token": token
        });

        var requestBody = JSON.stringify(requestData);

        var request = https.request(options, function (response) {

            // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
            if (response.statusCode !== 200) {
                console.log("Process the image defog  failed");
                return;
            }

            // 拼接返回结果的base64的字符串
            var resultStr = "";
            response.on("data", function (chunk) {
                resultStr += chunk.toString();
            });

            response.on("end", function () {
                callback(resultStr);
            })
        });

        request.on("error", function (err) {
            console.log(err.message);
        });

        request.write(requestBody);
        request.end();
    },

    defog_aksk: function (_ak, _sk, data, gamma, natural_look, callback) {

        // 配置ak，sk信息
        var sig = new signer.Signer();
        sig.AppKey = _ak;                   // 构建ak
        sig.AppSecret = _sk;                // 构建sk
        /**
         *  构建请求信息
         * image: 图片信息 base64
         * file：与image 二选一 图片文件
         * gamma：gamma 矫正值
         * natural_look：可选 ，是否保持自然感官
         * @type {string}
         */
        var requestData = {"image": data, "file": "", "gamma": 1.5, "natural_look": true};
        var req = new signer.HttpRequest();
        var options = utils.getHttpRequestEntity(sig, req, "ais.cn-north-1.myhwclouds.com", "POST", "/v1.0/vision/defog", "", {"Content-Type": "application/json"}, requestData);

        var request = https.request(options, function (response) {

            // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
            if (response.statusCode !== 200) {
                console.log("Process the image defog  failed!");
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