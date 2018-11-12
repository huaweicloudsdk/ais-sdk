/**
 * 图像内容检测服务的使用示例
 */
var content = require("./ais_sdk/image_content");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*******";       // 配置用户名
var domainname = "*******";     // 配置用户名
var password = "*******";       // 密码
var regionName = "cn-north-1";  // 配置区域信息

var filepath = "./data/moderation-terrorism.jpg";
var data = utils.changeFileToBase64(filepath);

demo_data_url = "https://ais-sample-data.obs.cn-north-1.myhwclouds.com/terrorism.jpg";
/**
 * token 方式获取结果
 * @type {string}
 */

token.getToken(username, domainname, password, regionName, function (token) {

    content.image_content(token, data, "", ["politics"], "", function (result) {
        console.log(result);
    });

    content.image_content(token, "", demo_data_url, ["politics"], "", function (result) {
        console.log(result);
    })
});
/**
 * aksk 方式获取结果
 * @type {string}
 */
var app_key = "*************";
var app_secret = "************";

content.image_content_aksk(app_key, app_secret, data, "", ["politics"], "", function (result) {
    console.log(result);
});

content.image_content_aksk(app_key, app_secret, "", demo_data_url, ["politics"], "", function (result) {
    console.log(result);
});