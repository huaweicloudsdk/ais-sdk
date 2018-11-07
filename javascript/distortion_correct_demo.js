/**
 * 扭曲校正服务的使用示例
 */
var discor = require("./ais_sdk/distortion_correct");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*******";       // 配置用户名
var domainname = "*******";     // 配置用户名
var password = "*******";       // 密码
var regionName = "cn-north-1";  // 配置区域信息

var filepath = "./data/modeation_distortion.jpg";
var data = utils.changeFileToBase64(filepath);

/**
 * token 方式获取结果
 * @type {string}
 */
demo_data_url = "https://ais-sample-data.obs.myhwclouds.com/tagging-normal.jpg";
token.getToken(username, domainname, password, regionName, function (token) {

    discor.distortion_correct(token, data, "", true, function (result) {
        var resultObj = JSON.parse(result);

        if (resultObj.result.data !== "") {
            utils.getFileByBase64Str("./data/modeation_distortion-1.jpg", resultObj.result.data);
        }else{
            console.log(result);
        }
    });

    discor.distortion_correct(token, "", demo_data_url, true, function (result) {
        var resultObj = JSON.parse(result);

        if (resultObj.result.data !== "") {
            utils.getFileByBase64Str("./data/modeation_distortion-2.jpg", resultObj.result.data);
        }else{
            console.log(result);
        }
    });
});

/**
 * aksk 方式获取结果
 * @type {string}
 */
var app_key = "*************";
var app_secret = "************";

discor.distortion_correct_aksk(app_key, app_secret, data, "", true, function (result) {
    var resultObj = JSON.parse(result);

    if (resultObj.result.data !== "") {
        utils.getFileByBase64Str("./data/modeation_distortion-2.jpg", resultObj.result.data);
    }else{
        console.log(result);
    }
});

discor.distortion_correct_aksk(app_key, app_secret, "", demo_data_url, true, function (result) {
    var resultObj = JSON.parse(result);

    if (resultObj.result.data !== "") {
        utils.getFileByBase64Str("./data/modeation_distortion-2.jpg", resultObj.result.data);
    }else{
        console.log(result);
    }
});