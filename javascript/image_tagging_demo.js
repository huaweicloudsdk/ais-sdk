/**
 * 图像标签检测服务的使用示例
 */
var tagging = require("./ais_sdk/imager_tagging");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*******";       // 配置用户名
var domainname = "*******";     // 配置用户名
var password = "*******";       // 密码
var regionName = "cn-north-1";  // 配置区域信息

var filepath = "./data/image-tagging-demo.jpg";
var data = utils.changeFileToBase64(filepath);

/**
 * token 方式获取结果
 * @type {string}
 */
demo_data_url = "https://ais-sample-data.obs.myhwclouds.com/tagging-normal.jpg";
token.getToken(username, domainname, password, regionName, function (token) {

    tagging.image_tagging(token, data, "", 60, "en", 5, function (result) {
        console.log(result);
    });

    tagging.image_tagging(token, "", demo_data_url, 60, "en", 5, function (result) {
        console.log(result);
    })
});

/**
 *  aksk 方式获取结果
 * @type {string}
 */
var app_key = "*************";
var app_secret = "************";

tagging.image_tagging_aksk(app_key, app_secret, data, "", 60, "en", 5, function (result) {
    console.log(result);
});

tagging.image_tagging_aksk(app_key, app_secret, "", demo_data_url, 60, "en", 5, function (result) {
    console.log(result);
});