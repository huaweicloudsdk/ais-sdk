/**
 *  获取token信息的方法
 */
var https = require("https");   // 加载node.js内置的https的模块
var utils = require("./utils");
var ais = require("./ais");

module.exports = {

    getToken: function (username, domainname, password, callback) {

        // 构建获取token的请求信息
        var requestBody = utils.getRequestBodyForToken(username, password, domainname);
        var host = ais.IAM_ENPOINT;
        var uri = ais.AIS_TOKEN;
        var options = utils.getHttpRequestEntityOptions(host, "POST", uri, {"Content-Type": "application/json"});
            var request = https.request(options, function (response) {

                // 通过Header,获取token信息
                var token = response.headers['x-subject-token'];
                callback(token);
            });

            request.on("error", function (err) {
                console.log(err.message);
            });

            request.write(requestBody);
            request.end();
        }
};
