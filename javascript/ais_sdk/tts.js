var https = require("https");
var utils = require("./utils");
var signer = require("./signer");
var ais = require("./ais");

module.exports = {
    tts: function (token, text, voice_name, volume, sample_rate, speech_speed, pitch_rate, callback) {

        // 构建请求信息和请求参数信息
        var requestData = {
            "text": text,                                // text :待合成的文本
            "voice_name": voice_name,                   // voice_name:合成的声音人员表示
            "volume": volume,                           // volume：音量
            "sample_rate": sample_rate,                 // sample_rate：语音的采样率，目前支持16k，代表16HZ
            "speech_speed": speech_speed,               // peech_speed：语速 [-500,500]
            "pitch_rate": pitch_rate                    // pitch_rate：音高 [-20,20]
        };
        var options = utils.getHttpRequestEntityOptions(ais.ENDPOINT, "POST", ais.TTS, {
            "Content-Type": "application/json",
            "X-Auth-Token": token
        });

        var requestBody = JSON.stringify(requestData);

        var request = https.request(options, function (response) {

            // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
            if (response.statusCode !== 200) {
                console.log("Process the tts result failed!");
                return;
            }

            var resultStr = "";

            // 拼接返回结果的base64的字符串
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

    tts_aksk: function (_ak, _sk, text, voice_name, volume, sample_rate, speech_speed, pitch_rate, callback) {

        // 配置ak，sk信息
        var sig = new signer.Signer();
        sig.AppKey = _ak;               // 构建ak
        sig.AppSecret = _sk;            // 构建sk

        var requestData = {
            "text": text,                                 // text :待合成的文本
            "voice_name": voice_name,                    // voice_name:合成的声音人员表示
            "volume": volume,                            // volume：音量
            "sample_rate": sample_rate,                  // sample_rate：语音的采样率，目前支持16k，代表16HZ
            "speech_speed": speech_speed,                // speech_speed：语速 [-500,500]
            "pitch_rate": pitch_rate                     // pitch_rate：音高 [-20,20]
        };
        var req = new signer.HttpRequest();
        var options = utils.getHttpRequestEntity(sig, req, ais.ENDPOINT, "POST", ais.TTS, "", {"Content-Type": "application/json"}, requestData);

        var request = https.request(options, function (response) {

            // 验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
            if (response.statusCode !== 200) {
                console.log("Process the tts result failed!");
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