/**
 * 低光照增强服务token 方式请求的使用示例
 */
var enhance = require("./ais_sdk/dark_enhance");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*******";        // 配置用户名
var domain_name = "*******";     // 配置用户名
var password = "*******";        // 密码
var region_name = "cn-north-1";  // 配置区域信息

var filepath = "./data/dark-enhance-demo.bmp";
var data = utils.changeFileToBase64(filepath);

token.getToken(username, domain_name, password, region_name, function (token) {

    enhance.dark_enhance(token, data, 0.9, function (result) {
        var resultObj = JSON.parse(result);
        utils.getFileByBase64Str("./data/dark-enhance-demo-token.bmp", resultObj.result);
    })
});
