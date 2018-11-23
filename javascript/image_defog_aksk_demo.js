/**
 * 图像去雾服务ak,sk请求方式的使用示例
 */
var defog = require("./ais_sdk/image_defog");
var utils = require("./ais_sdk/utils");

var app_key = "*************";
var app_secret = "************";

var filepath = "./data/defog-demo.png";
var data = utils.changeFileToBase64(filepath);

defog.defog_aksk(app_key, app_secret, data, 1.5, true, function (result) {
    var resultObj = JSON.parse(result);
    utils.getFileByBase64Str("./data/defog-demo-aksk.png", resultObj.result);
});

