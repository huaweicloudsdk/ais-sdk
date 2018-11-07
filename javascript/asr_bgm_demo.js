/**
 * 背景音乐识别服务的使用示例
 */
var bgm = require("./ais_sdk/asr_bgm");
var token = require("./ais_sdk/gettoken");

var username = "*******";       // 配置用户名
var domainname = "*******";     // 配置用户名
var password = "*******";       // 密码
var regionName = "cn-north-1";  // 配置区域信息

/**
 * token 方式获取结果
 * @type {string}
 */
obsUrl = "https://obs-test-llg.obs.cn-north-1.myhwclouds.com/bgm_recognition";
token.getToken(username, domainname, password, regionName, function (token) {

    bgm.asr_bgm(token, obsUrl, function (result) {
        console.log(result);
    });
});

/**
 * aksk 方式获取结果
 * @type {string}
 */
var app_key = "**************";
var app_secret = "************";

bgm.asr_bgm_aksk(app_key, app_secret, obsUrl, function (result) {
    console.log(result);
});
