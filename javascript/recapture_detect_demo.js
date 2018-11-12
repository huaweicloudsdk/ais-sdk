/**
 * 翻拍识别服务的使用示例
 */
var recapture = require("./ais_sdk/recapture_detect");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*******";       // 配置用户名
var domainname = "*******";     // 配置用户名
var password = "*******";       // 密码
var regionName = "cn-north-1";  // 配置区域信息

var filepath = "./data/recapture-detect-demo.jpg";
var data = utils.changeFileToBase64(filepath);

/**
 * token 方式获取结果
 * @type {string}
 */
demo_data_url = "https://ais-sample-data.obs.myhwclouds.com/recapture-detect.jpg";
token.getToken(username, domainname, password, regionName, function (token) {

    recapture.recapture_detect(token, data, "", 0.99, ['recapture'], function (result) {
        console.log(result);
    });

    recapture.recapture_detect(token, "", demo_data_url, 0.99, ['recapture'], function (result) {
        console.log(result);
    })
});

/**
 * aksk 方式获取结果
 * @type {string}
 */
var app_key = "*************";
var app_secret = "************";

recapture.recapture_detect_aksk(app_key, app_secret, data, "", 0.99, ['recapture'], function (result) {
    console.log(result);
});

recapture.recapture_detect_aksk(app_key, app_secret, "", demo_data_url, 0.99, ['recapture'], function (result) {
    console.log(result);
});