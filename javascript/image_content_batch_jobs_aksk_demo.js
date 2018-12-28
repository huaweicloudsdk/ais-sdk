/**
 * 图像内容检测批量异步服务ak,sk请求方式的使用示例
 */
var content = require("./ais_sdk/batch_jobs");
var utils = require("./ais_sdk/utils");

var app_key = "*************";
var app_secret = "*************";

var filepath = "./data/moderation-terrorism.jpg";
var data = utils.changeFileToBase64(filepath);

url1 = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg";
url2 = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg";

content.batch_jobs_aksk(app_key, app_secret, [url1, url2], ["politics", "terrorism", "porn"], function (result) {
    console.log(result);
});