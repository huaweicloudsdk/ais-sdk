/**
 * 图像去雾服务token 方式请求的使用示例
 */
var defog = require("./ais_sdk/image_defog");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*************";        // 配置用户名
var domain_name = "*************";     // 配置用户名
var password = "*************";        // 密码
var region_name = "cn-north-1";        // 配置区域信息

var filepath = "./data/defog-demo.png";
var data = utils.changeFileToBase64(filepath);

token.getToken(username, domain_name, password, region_name, function (token) {

    defog.defog(token, data, 1.5, true, function (result) {
        var resultObj = JSON.parse(result);
        utils.getFileByBase64Str("./data/defog-demo-token.png", resultObj.result);
    })
});
