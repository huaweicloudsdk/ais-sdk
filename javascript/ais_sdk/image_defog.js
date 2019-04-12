var https = require("https");
var utils = require("./utils");
var signer = require("./signer");
var ais = require("./ais");

module.exports = {
    defog: function (token, data, gamma, natural_look, callback) {

        var endPoint = utils.getEndPoint(ais.IMAGE_SERVICE);

        /**
         *  构建请求信息
         * image: 图片信息 base64
         * file：与image 二选一 图片文件
         * gamma：gamma 矫正值
         * natural_look：可选 ，是否保持自然感官
         * @type {string}
         */
        var requestData = {"image": data, "gamma": gamma, "natural_look": natural_look};
        var requestBody = JSON.stringify(requestData);

        var host = endPoint;
        var method = "POST";
        var uri = ais.DEFOG;
        var headers = {'Content-Type': 'application/json', 'X-Auth-Token': token, 'Content-Length':requestBody.length};
        var options = utils.getHttpRequestEntityOptions(host, method, uri, headers);

        var request = https.request(options, function (response) {

            var resultStr = "";
            // 拼接返回结果的base64的字符串
            response.on("data", function (chunk) {
                // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
                if (response.statusCode !== 200) {
                    console.log('Http status code is: ' + response.statusCode);
                    console.log(chunk.toString());
                    return;
                }
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

        var endPoint = utils.getEndPoint(ais.IMAGE_SERVICE);
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
        var options = utils.getHttpRequestEntity(sig, req, endPoint, "POST", ais.DEFOG, "", {"Content-Type": "application/json"}, requestData);

        var request = https.request(options, function (response) {

            // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
            if (response.statusCode !== 200) {
                console.log('Http status code is: ' + response.statusCode);
            }

            var resultStr = "";
            // 拼接返回结果的base64的字符串
            response.on("data", function (chunk) {
                // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
                if (response.statusCode !== 200) {
                    console.log('Http status code is: ' + response.statusCode);
                    console.log(chunk.toString());
                    return;
                }
                resultStr += chunk.toString();
            });

            response.on("end", function () {
                callback(resultStr);
            })
        });

        request.on("error", function (err) {
            console.log(err.message);
        });

        request.write(req.body);
        request.end();
    }
};