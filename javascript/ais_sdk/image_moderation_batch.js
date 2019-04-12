var https = require("https");
var utils = require("./utils");
var signer = require("./signer");
var ais = require("./ais");

module.exports = {
    image_content_batch: function (token, urls, categories, threshold, callback) {

        var endPoint = utils.getEndPoint(ais.MODERATION_SERVICE);

        /*
         * urls：  图片的obs对象路径地址
         * threshold：非必选 结果过滤门限
         * categories：非必选 检测场景 array politics：是否涉及政治人物的检测。terrorism：是否包含暴恐元素的检测。porn：是否包含涉黄内容元素的检测。
         * @type {string}
         */
        var requestData = {urls: urls, "categories": categories, "threshold": threshold};
        var requestBody = JSON.stringify(requestData);

        var options = utils.getHttpRequestEntityOptions(endPoint, "POST", ais.IMAGE_CONTENT_BATCH, {
            "Content-Type": "application/json",
            "X-Auth-Token": token,
            "Content-Length": requestBody.length
        });

        var request = https.request(options, function (response) {

            // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
            if (response.statusCode !== 200) {
                console.log('Http status code is: ' + response.statusCode);
            }

            // 返回图像内容检测服务结果
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

    image_content_batch_aksk: function (_ak, _sk, urls, categories, threshold, callback) {

        // 配置ak，sk信息
        var sig = new signer.Signer();
        sig.AppKey = _ak;                   // 构建ak
        sig.AppSecret = _sk;                // 构建sk

        var endPoint = utils.getEndPoint(ais.MODERATION_SERVICE);

        // 构建请求信息和请求参数信息
        /**
         *  url ： 与image二选一 图片的URL路径
         *  threshold ：非必选 结果过滤门限
         *  categories ：非必选 检测场景 array politics：是否涉及政治人物的检测。terrorism：是否包含暴恐元素的检测。porn：是否包含涉黄内容元素的检测。
         * @type {string}
         */
        var requestData = {"urls": urls, "categories": categories, "threshold": threshold};
        var req = new signer.HttpRequest();
        var options = utils.getHttpRequestEntity(sig, req, endPoint, "POST", ais.IMAGE_CONTENT_BATCH, "", {"Content-Type": "application/json"}, requestData);

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