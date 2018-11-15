/**
 * 图像反黄检测服务token 方式请求的使用示例
 */
var antiporn = require("./ais_sdk/image_antiporn");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*******";       // 配置用户名
var domainname = "*******";     // 配置用户名
var password = "*******";       // 密码
var regionName = "cn-north-1";  // 配置区域信息

var filepath = "./data/moderation-antiporn.jpg";
var data = utils.changeFileToBase64(filepath);

demo_data_url = 'https://ais-sample-data.obs.cn-north-1.myhwclouds.com/antiporn.jpg';
token.getToken(username, domainname, password, regionName, function (token) {

    antiporn.image_antiporn(token, data, "", function (result) {
        console.log(result);
    });

    antiporn.image_antiporn(token, "", demo_data_url, function (result) {
        console.log(result);
    })
});