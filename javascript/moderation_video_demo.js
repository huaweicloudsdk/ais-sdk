/**
 * 视频审核服务的使用示例
 */
var video = require("./ais_sdk/moderation_video");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*******";       // 配置用户名
var domainname = "*******";     // 配置用户名
var password = "*******";       // 密码
var regionName = "cn-north-1";  // 配置区域信息

demo_data_url = "https://obs-test-llg.obs.cn-north-1.myhwclouds.com/bgm_recognition";

/**
 * token 方式获取结果
 * @type {string}
 */
token.getToken(username, domainname, password, regionName, function (token) {

    video.video(token, demo_data_url, 5, ["terrorism", "porn", "politics"], function (result) {
        console.log(result);
    })

});

/**
 * aksk 方式获取结果
 * @type {string}
 */
var app_key = "*************";
var app_secret = "************";

video.video_aksk(app_key, app_secret, demo_data_url, 5, ["terrorism", "porn", "politics"], function (result) {
    console.log(result)
});


