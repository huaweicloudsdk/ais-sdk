/**
 * 长语音识别服务ak，sk请求方式的使用示例
 */
var lsen = require("./ais_sdk/long_sentence");
var utils = require("./ais_sdk/utils");

var app_key = "*************";
var app_secret = "************";

demo_data_url = "https://obs-ch-sdk-sample.obs.cn-north-1.myhwclouds.com/lsr-1.mp3";

var filepath = "./data/asr-sentence.wav";
var data = utils.changeFileToBase64(filepath);

lsen.long_sentence_aksk(app_key, app_secret, "", demo_data_url, "", function (result) {
    console.log(result)
});

lsen.long_sentence_aksk(app_key, app_secret, data, "", "", function (result) {
    console.log(result)
});

