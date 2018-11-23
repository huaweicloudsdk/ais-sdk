/**
 *  图像内容检测服务token请求方式的使用示例
 */
var text = require("./ais_sdk/moderation_text");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*******";        // 配置用户名
var domain_name = "*******";     // 配置用户名
var password = "*******";        // 密码
var region_name = "cn-north-1";  // 配置区域信息

token.getToken(username, domain_name, password, region_name, function (token) {

    text.moderation_text(token, [{
            "text": "666666luo聊请+110亚砷酸钾六位qq，fuck666666666666666",
            "type": "content"
        }],
        ["ad", "politics", "politics", "politics", "contraband", "contraband"], function (result) {
            console.log(result);
        })

});
