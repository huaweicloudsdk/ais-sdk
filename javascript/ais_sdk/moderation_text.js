var https = require("https");
var utils = require("./utils");
var signer = require("./signer");
var ais = require("./ais");

module.exports = {
    moderation_text: function (token, items, categories, callback) {

        var endPoint = utils.getEndPoint(ais.MODERATION_SERVICE);

        // 构建请求信息和请求参数信息
        var requestData = {
            "categories": categories,       // 检测场景 Array politics：涉政 porn：涉黄 ad：广告 abuse：辱骂 contraband：违禁品 flood：灌水
            "items": items                  // items: 待检测的文本列表  text 待检测文本 type 文本类型
        };
        var options = utils.getHttpRequestEntityOptions(endPoint, "POST", ais.MODERATION_TEXT, {
            "Content-Type": "application/json",
            "X-Auth-Token": token
        });
        var requestBody = JSON.stringify(requestData);

        var request = https.request(options, function (response) {

            // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
            if (response.statusCode !== 200) {
                console.log('Http status code is: ' + response.statusCode);
            }

            // 返回文本内容检测服务结果
            response.on("data", function (chunk) {
                // 返回中文unicode处理
                var result = JSON.parse(chunk.toString());
                callback(JSON.stringify(result));
            })
        });

        request.on("error", function (err) {
            console.log(err.message);
        });

        request.write(requestBody);
        request.end();
    },

    moderation_text_aksk: function (_ak, _sk, items, categories, callback) {

        var endPoint = utils.getEndPoint(ais.MODERATION_SERVICE);

        // 配置ak，sk信息
        var sig = new signer.Signer();
        sig.AppKey = _ak;                   //构建ak
        sig.AppSecret = _sk;                //构建sk

        // 构建请求信息和请求参数信息
        var requestData = {
            "categories": categories,   // 检测场景 Array politics：涉政 porn：涉黄 ad：广告 abuse：辱骂 contraband：违禁品 flood：灌水
            "items": items              // items: 待检测的文本列表  text 待检测文本 type 文本类型
        };
        var req = new signer.HttpRequest();
        var options = utils.getHttpRequestEntity(sig, req, endPoint, "POST", ais.MODERATION_TEXT, "", {"Content-Type": "application/json"}, requestData);

        var request = https.request(options, function (response) {

            // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
            if (response.statusCode !== 200) {
                console.log('Http status code is: ' + response.statusCode);
            }

            // 返回图像内容检测服务结果信息
            response.on("data", function (chunk) {
                // 返回中文unicode处理
                var result = JSON.parse(chunk.toString());
                callback(JSON.stringify(result));
            })
        });

        request.on("error", function (err) {
            console.log(err.message);
        });

        request.write(req.body);
        request.end();

    }
};