/**
 * 图像标签检测服务token 方式请求的使用示例
 */
var tagging = require("./ais_sdk/image_tagging");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*******";        // 配置用户名
var domain_name = "*******";     // 配置用户名
var password = "*******";        // 密码
var region_name = "cn-north-1";  // 配置区域信息

var filepath = "./data/image-tagging-demo.jpg";
var data = utils.changeFileToBase64(filepath);

demo_data_url = "https://ais-sample-data.obs.myhuaweicloud.com/tagging-normal.jpg";

token.getToken(username, domain_name, password, region_name, function (token) {

    tagging.image_tagging(token, data, "", 60, "en", 5, function (result) {
        console.log(result);
    });

    tagging.image_tagging(token, "", demo_data_url, 60, "en", 5, function (result) {
        console.log(result);
    })
});