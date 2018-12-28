/**
 * 图像内容检测服务token请求方式的使用示例
 */
var content = require("./ais_sdk/image_content");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*******";        // 配置用户名
var domain_name = "*******";     // 配置用户名
var password = "*******";        // 密码
var region_name = "cn-north-1";  // 配置区域信息

var filepath = "./data/moderation-terrorism.jpg";
var data = utils.changeFileToBase64(filepath);

demo_data_url = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg";

token.getToken(username, domain_name, password, region_name, function (token) {

    content.image_content(token, data, "", ["politics"], "", function (result) {
        console.log(result);
    });

    content.image_content(token, "", demo_data_url, ["politics"], "", function (result) {
        console.log(result);
    })
});