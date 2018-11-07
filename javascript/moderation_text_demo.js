/**
 *  图像内容检测服务的使用示例
 */
var text = require("./ais_sdk/moderation_text");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*******";       // 配置用户名
var domainname = "*******";     // 配置用户名
var password = "*******";       // 密码
var regionName = "cn-north-1";  // 配置区域信息

/**
 * token 方式获取结果
 * @type {string}
 */
token.getToken(username, domainname, password, regionName, function (token) {

    text.moderation_text(token, [{
            "text": "666666luo聊请+110亚砷酸钾六位qq，fuck666666666666666",
            "type": "content"
        }],
        ["ad", "politics", "politics", "politics", "contraband", "contraband"], function (result) {
            console.log(result);
        })

});

/**
 *  aksk 方式获取结果
 * @type {string}
 */
var app_key = "*************";
var app_secret = "************";

text.moderation_text_aksk(app_key, app_secret, [{
    "text": "666666luo聊请+110亚砷酸钾六位qq，fuck666666666666666",
    "type": "content"
}], ["ad", "politics", "politics", "politics", "contraband", "contraband"], function (result) {
    console.log(result);
});
