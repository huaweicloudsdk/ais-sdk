/**
 * 视频审核服务的ak,sk请求方式使用示例
 */
var video = require("./ais_sdk/moderation_video");
var utils = require("./ais_sdk/utils");

// 初始化服务的区域信息，目前支持华北-北京一(cn-north-1)、亚太-香港(ap-southeast-1)等区域信息
utils.initRegion(region="cn-north-1");

var app_key = "*************";
var app_secret = "************";

// obs链接需要和region区域一致，不同的region的obs资源不共享
demo_data_url = "https://obs-test-llg.obs.cn-north-1.myhuaweicloud.com/bgm_recognition";

video.video_aksk(app_key, app_secret, demo_data_url, 5, ["terrorism", "porn", "politics"], function (result) {
    console.log(result)
});


