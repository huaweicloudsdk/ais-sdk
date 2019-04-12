/**
 * 图像反黄检测服务token 方式请求的使用示例
 */
var antiporn = require("./ais_sdk/image_antiporn");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

// 初始化服务的区域信息，目前支持北京1(cn-north-1)、香港(ap-southeast-1)等区域信息
utils.initRegion(region="cn-north-1");

var username = "*******";        // 配置用户名
var domain_name = "*******";     // 配置用户名
var password = "*******";        // 密码

var filepath = "./data/moderation-antiporn.jpg";
var data = utils.changeFileToBase64(filepath);

demo_data_url = 'https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg';
token.getToken(username, domain_name, password, function (token) {

    antiporn.image_antiporn(token, data, "", function (result) {
        console.log(result);
    });

    antiporn.image_antiporn(token, "", demo_data_url, function (result) {
        console.log(result);
    })
});