/**
 * 图像清晰度检测服务token 方式请求的使用示例
 */
var clarity = require("./ais_sdk/clarity_detect");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*******";       // 配置用户名
var domainname = "*******";     // 配置用户名
var password = "*******";       // 密码
var regionName = "cn-north-1";  // 配置区域信息

var filepath = "./data/moderation-clarity.jpg";
var data = utils.changeFileToBase64(filepath);

demo_data_url = "https://ais-sample-data.obs.cn-north-1.myhwclouds.com/vat-invoice.jpg";

token.getToken(username, domainname, password, regionName, function (token) {

    clarity.clarity_detect(token, data, "", 0.8, function (result) {
        console.log(result);
    });

    clarity.clarity_detect(token, "", demo_data_url, 0.8, function (result) {
        console.log(result);
    });
});
