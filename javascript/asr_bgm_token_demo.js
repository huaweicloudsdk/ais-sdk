/**
 * 背景音乐识别服务token 方式请求的使用示例
 */
var bgm = require("./ais_sdk/asr_bgm");
var token = require("./ais_sdk/gettoken");

var username = "*******";        // 配置用户名
var domain_name = "*******";     // 配置用户名
var password = "*******";        // 密码
var region_name = "cn-north-1";  // 配置区域信息

obsUrl = "https://obs-test-llg.obs.cn-north-1.myhuaweicloud.com/bgm_recognition";
token.getToken(username, domain_name, password, region_name, function (token) {

    bgm.asr_bgm(token, obsUrl, function (result) {
        console.log(result);
    });
});