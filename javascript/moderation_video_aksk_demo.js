/**
 * 视频审核服务的ak,sk请求方式使用示例
 */
var video = require("./ais_sdk/moderation_video");
var utils = require("./ais_sdk/utils");

var app_key = "*************";
var app_secret = "************";

demo_data_url = "https://obs-test-llg.obs.cn-north-1.myhwclouds.com/bgm_recognition";

video.video_aksk(app_key, app_secret, demo_data_url, 5, ["terrorism", "porn", "politics"], function (result) {
    console.log(result)
});


