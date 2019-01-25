/**
 * 图像内容批量检测服务token请求方式的使用示例
 */
var content = require("./ais_sdk/image_moderation_batch");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*******";        // 配置用户名
var domain_name = "*******";     // 配置用户名
var password = "*******";        // 密码
var region_name = "cn-north-1";  // 配置区域信息

url1 = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg";
url2 = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg";

token.getToken(username, domain_name, password, region_name, function (token) {

    content.image_content_batch(token, [url1,url2], ["politics", "terrorism", "porn"], 0, function (result) {
        console.log(result);
    });

});