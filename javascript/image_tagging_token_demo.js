/**
 * 图像标签检测服务token 方式请求的使用示例
 */
var tagging = require("./ais_sdk/image_tagging");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

// 初始化服务的区域信息，目前支持北京1(cn-north-1)、香港(ap-southeast-1)等区域信息
utils.initRegion(region="cn-north-1");

var username = "*******";        // 配置用户名
var domain_name = "*******";     // 配置用户名
var password = "*******";        // 密码

var filepath = "./data/image-tagging-demo.jpg";
var data = utils.changeFileToBase64(filepath);

// obs链接需要和region区域一致，不同的region的obs资源不共享
demo_data_url = "https://ais-sample-data.obs.myhuaweicloud.com/tagging-normal.jpg";

token.getToken(username, domain_name, password, function (token) {

    tagging.image_tagging(token, data, "", 60, "en", 5, function (result) {
        console.log(result);
    });

    tagging.image_tagging(token, "", demo_data_url, 60, "en", 5, function (result) {
        console.log(result);
    })
});