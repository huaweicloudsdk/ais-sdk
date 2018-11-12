/**
 * 图像去雾服务的使用示例
 */
var defog = require("./ais_sdk/image_defog");
var token = require("./ais_sdk/gettoken");
var utils = require("./ais_sdk/utils");

var username = "*******";       // 配置用户名
var domainname = "*******";     // 配置用户名
var password = "*******";       // 密码
var regionName = "cn-north-1";  // 配置区域信息

var filepath = "./data/defog-demo.png";
var data = utils.changeFileToBase64(filepath);
/**
 * token 方式获取结果
 * @type {string}
 */
token.getToken(username, domainname, password, regionName, function (token) {

    defog.defog(token, data, 1.5, true, function (result) {
        var resultObj = JSON.parse(result);
        utils.getFileByBase64Str("./data/defog-demo-token.png", resultObj.result);
    })
});

/**
 * aksk 方式获取结果
 * @type {string}
 */
var app_key = "*************";
var app_secret = "************";

defog.defog_aksk(app_key, app_secret, data, 1.5, true, function (result) {
    var resultObj = JSON.parse(result);
    utils.getFileByBase64Str("./data/defog-demo-aksk.png", resultObj.result);
});

