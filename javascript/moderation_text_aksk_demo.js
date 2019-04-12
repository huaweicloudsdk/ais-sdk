/**
 *  图像内容检测服务ak,sk方式请求的使用示例
 */
var text = require("./ais_sdk/moderation_text");
var utils = require("./ais_sdk/utils");

// 初始化服务的区域信息，目前支持北京1(cn-north-1)、香港(ap-southeast-1)等区域信息
utils.initRegion(region="cn-north-1");

var app_key = "*************";
var app_secret = "************";

text.moderation_text_aksk(app_key, app_secret, [{
    "text": "666666luo聊请+110亚砷酸钾六位qq，fuck666666666666666",
    "type": "content"
}], ["ad", "abuse", "politics", "porn", "contraband"], function (result) {
    console.log(result);
});
