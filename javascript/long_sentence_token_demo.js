/**
 * 长语音识别服务token请求方式的使用示例
 */
var lsen = require("./ais_sdk/long_sentence");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*******";        // 配置用户名
var domain_name = "*******";     // 配置用户名
var password = "*******";        // 密码

demo_data_url = "https://obs-ch-sdk-sample.obs.cn-north-1.myhwclouds.com/lsr-1.mp3";

var filepath = "./data/asr-sentence.wav";
var data = utils.changeFileToBase64(filepath);

token.getToken(username, domain_name, password, function (token) {

    lsen.long_sentence(token, "", demo_data_url, "", function (result) {
        console.log(result)
    });

    lsen.long_sentence(token, data, "", "", function (result) {
        console.log(result)
    })
});

