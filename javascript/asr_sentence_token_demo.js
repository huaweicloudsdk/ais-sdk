/**
 * 语音识别服务token 方式请求的使用示例
 */
var sentence = require("./ais_sdk/asr_sentence");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*******";        // 配置用户名
var domain_name = "*******";     // 配置用户名
var password = "*******";        // 密码
var region_name = "cn-north-1";  // 配置区域信息

var filepath = "./data/asr-sentence.wav";
var data = utils.changeFileToBase64(filepath);

obsUrl = "https://ais-sample-data.obs.myhwclouds.com/asr-sentence.wav";
token.getToken(username, domain_name, password, region_name, function (token) {

    sentence.asr_scentence(token, data, "", "wav", "16k", function (result) {
        console.log(result);
        console.log(JSON.parse(result).result.words)
    });

    sentence.asr_scentence(token, "", obsUrl, "wav", "16k", function (result) {
        console.log(result);
        console.log(JSON.parse(result).result.words)
    })
});

