/**
 * 长语音识别服务的使用示例
 */
var lsen = require("./ais_sdk/long_sentence");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*******";       // 配置用户名
var domainname = "*******";     // 配置用户名
var password = "*******";       // 密码
var regionName = "cn-north-1";  // 配置区域信息

demo_data_url = "https://ais-sample-data.obs.myhwclouds.com/lsr-1.mp3";

var filepath = "./data/asr-sentence.wav";
var data = utils.changeFileToBase64(filepath);

/**
 * token 方式获取结果
 * @type {string}
 */
token.getToken(username, domainname, password, regionName, function (token) {

    lsen.long_sentence(token, "", demo_data_url, "", function (result) {
        console.log(result)
    });

    lsen.long_sentence(token, data, "", "", function (result) {
        console.log(result)
    })
});

/**
 * aksk 方式获取结果
 * @type {string}
 */
var app_key = "*************";
var app_secret = "************";

lsen.long_sentence_aksk(app_key, app_secret, "", demo_data_url, "", function (result) {
    console.log(result)
});

lsen.long_sentence_aksk(app_key, app_secret, data, "", "", function (result) {
    console.log(result)
});

