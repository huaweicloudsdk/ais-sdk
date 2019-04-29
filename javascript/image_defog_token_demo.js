/**
 * 图像去雾服务token 方式请求的使用示例
 */
var defog = require("./ais_sdk/image_defog");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

// 初始化服务的区域信息，目前支持华北-北京一(cn-north-1)、亚太-香港(ap-southeast-1)等区域信息
utils.initRegion("cn-north-1");

var username = "*************";        // 配置用户名
var domain_name = "*************";     // 配置用户名
var password = "*************";        // 密码

var filepath = "./data/defog-demo.png";
var data = utils.changeFileToBase64(filepath);

token.getToken(username, domain_name, password, function (token) {

    defog.defog(token, data, 1.5, true, function (result) {
        var resultObj = JSON.parse(result);
        utils.getFileByBase64Str("./data/defog-demo-token.png", resultObj.result);
    })
});
