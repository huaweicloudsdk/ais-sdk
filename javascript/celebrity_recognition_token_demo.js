/**
 * 名人识别检测服务token 方式请求的使用示例
 */
var recognition = require("./ais_sdk/celebrity_recognition");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*******";        // 配置用户名
var domain_name = "*******";     // 配置用户名
var password = "*******";        // 密码
var region_name = "cn-north-1";  // 配置区域信息

var filepath = "./data/celebrity-recognition.jpg";
var data = utils.changeFileToBase64(filepath);

demo_data_url = 'https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/celebrity-recognition.jpg';

token.getToken(username, domain_name, password, region_name, function (token) {

    recognition.celebrity_recognition(token, data, "", 0.48, function (result) {
        console.log(result);
    });

    recognition.celebrity_recognition(token, "", demo_data_url,0.48, function (result) {
        console.log(result);
    })
});