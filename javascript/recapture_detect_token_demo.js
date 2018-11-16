/**
 * 翻拍识别服务token请求方式的使用示例
 */
var recapture = require("./ais_sdk/recapture_detect");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*******";        // 配置用户名
var domain_name = "*******";     // 配置用户名
var password = "*******";        // 密码
var region_name = "cn-north-1";  // 配置区域信息

var filepath = "./data/recapture-detect-demo.jpg";
var data = utils.changeFileToBase64(filepath);

demo_data_url = "https://ais-sample-data.obs.myhwclouds.com/recapture-detect.jpg";

token.getToken(username, domain_name, password, region_name, function (token) {

    recapture.recapture_detect(token, data, "", 0.99, ['recapture'], function (result) {
        console.log(result);
    });

    recapture.recapture_detect(token, "", demo_data_url, 0.99, ['recapture'], function (result) {
        console.log(result);
    })
});
