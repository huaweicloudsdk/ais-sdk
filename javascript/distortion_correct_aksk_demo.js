/**
 * 扭曲校正服务ak,sk方式请求的使用示例
 */
var discor = require("./ais_sdk/distortion_correct");
var utils = require("./ais_sdk/utils");

// 初始化服务的区域信息，目前支持北京1(cn-north-1)、香港(ap-southeast-1)等区域信息
utils.initRegion(region="cn-north-1");

var app_key = "*************";
var app_secret = "************";

var filepath = "./data/modeation_distortion.jpg";
var data = utils.changeFileToBase64(filepath);

demo_data_url = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/vat-invoice.jpg";

discor.distortion_correct_aksk(app_key, app_secret, data, "", true, function (result) {
    var resultObj = JSON.parse(result);

    if (resultObj.result.data !== "") {
        utils.getFileByBase64Str("./data/modeation_distortion-aksk-1.jpg", resultObj.result.data);
    }else{
        console.log(result);
    }
});

discor.distortion_correct_aksk(app_key, app_secret, "", demo_data_url, true, function (result) {
    var resultObj = JSON.parse(result);

    if (resultObj.result.data !== "") {
        utils.getFileByBase64Str("./data/modeation_distortion-aksk-2.jpg", resultObj.result.data);
    }else{
        console.log(result);
    }
});