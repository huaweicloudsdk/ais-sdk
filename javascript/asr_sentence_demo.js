/**
 * 语音识别服务的使用示例
 */
var sentence = require("./ais_sdk/asr_sentence");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*******";       // 配置用户名
var domainname = "*******";     // 配置用户名
var password = "*******";       // 密码
var regionName = "cn-north-1";  // 配置区域信息

var filepath = "./data/asr-sentence.wav";
var data = utils.changeFileToBase64(filepath);

/**
 * token 方式获取结果
 * @type {string}
 */
obsUrl = "https://ais-sample-data.obs.myhwclouds.com/asr-sentence.wav";
token.getToken(username, domainname, password, regionName, function (token) {

    sentence.asr_scentence(token, data, "", "wav", "16k", function (result) {
        console.log(result);
        console.log(JSON.parse(result).result.words)
    });

    sentence.asr_scentence(token, "", obsUrl, "wav", "16k", function (result) {
        console.log(result);
        console.log(JSON.parse(result).result.words)
    })
});

/**
 *  aksk 方式获取结果
 * @type {string}
 */
var app_key = "*************";
var app_secret = "************";

sentence.asr_scentence_aksk(app_key, app_secret, data, "", "wav", "16k", function (result) {
    console.log(result);
    console.log(JSON.parse(result).result.words)
});

sentence.asr_scentence_aksk(app_key, app_secret, "", obsUrl, "wav", "16k", function (result) {
    console.log(result);
    console.log(JSON.parse(result).result.words)
});