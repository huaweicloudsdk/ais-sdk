/**
 * 图像去雾服务ak,sk请求方式的使用示例
 */
var defog = require("./ais_sdk/image_defog");
var utils = require("./ais_sdk/utils");

// 初始化服务的区域信息，目前支持北京1(cn-north-1)、香港(ap-southeast-1)等区域信息
utils.initRegion(region="cn-north-1");

var app_key = "*************";
var app_secret = "************";

var filepath = "./data/super-resolution-demo.png";
var data = utils.changeFileToBase64(filepath);

defog.defog_aksk(app_key, app_secret, data, 1.5, true, function (result) {
    var resultObj = JSON.parse(result);
    utils.getFileByBase64Str("./data/defog-demo-aksk.png", resultObj.result);
});

