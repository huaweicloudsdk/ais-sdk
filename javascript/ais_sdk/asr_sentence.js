var https = require("https");
var utils = require("./utils");
var signer = require("./signer");
var ais = require("./ais");

module.exports = {
    asr_scentence: function (token, data, url = "", encode_type = "wav", sample_rate = "16k", callback) {

        // 构建请求信息和请求参数信息
        var requestData = {"data": data, url: url, "encode_type": encode_type, "sample_rate": sample_rate};
        var headers = {"Content-Type": "application/json", "X-Auth-Token": token};
        var options = utils.getHttpRequestEntityOptions(ais.ENDPOINT, "POST", ais.ASR_SENTENCE, headers);
        var requestBody = JSON.stringify(requestData);

        var request = https.request(options, function (response) {

            // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
            if (response.statusCode !== 200) {
                console.log("Process the audio result failed!");
                return;
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

    asr_scentence_aksk: function (_ak, _sk, data, url = "", encode_type = "wav", sample_rate = "16k", callback) {

        // 配置ak，sk信息
        var sig = new signer.Signer();
        sig.AppKey = _ak;                   // 构建ak
        sig.AppSecret = _sk;                // 构建sk

        // 构建请求信息和请求参数信息
        var requestData = {"data": data, url: url, "encode_type": encode_type, "sample_rate": sample_rate};
        var _headers = {"Content-Type": "application/json"};
        var req = new signer.HttpRequest();
        var options = utils.getHttpRequestEntity(sig, req, ais.ENDPOINT, "POST", ais.ASR_SENTENCE, "", _headers, requestData);

        var requset = https.request(options, function (response) {

            //  验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
            if (response.statusCode !== 200) {
                console.log("Process the audio result failed!");
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