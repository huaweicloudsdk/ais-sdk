/**
 * 语音识别服务token 方式请求的使用示例
 */
var sentence = require("./ais_sdk/asr_sentence");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*******";        // 配置用户名
var domain_name = "*******";     // 配置用户名
var password = "*******";        // 密码

var filepath = "./data/asr-sentence.wav";
var data = utils.changeFileToBase64(filepath);

obsUrl = "https://obs-ch-sdk-sample.obs.cn-north-1.myhwclouds.com/asr-sentence.wav";
token.getToken(username, domain_name, password, function (token) {

    sentence.asr_scentence(token, data, "", "wav", "16k", function (result) {
        console.log(result);

    });

    sentence.asr_scentence(token, "", obsUrl, "wav", "16k", function (result) {
        console.log(result.toString());
    })
});

