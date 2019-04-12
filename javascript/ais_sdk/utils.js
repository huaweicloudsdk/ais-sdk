/**
 * js 封装通用方法
 */
var fs = require("fs");
var path = require("path");
var signer = require("./signer");
global.ENDPOINT = {
    'image': {
        'cn-north-1': 'image.cn-north-1.myhuaweicloud.com',
        'ap-southeast-1': 'image.ap-southeast-1.myhuaweicloud.com'
    },
    'moderation': {
        'cn-north-1': 'moderation.cn-north-1.myhuaweicloud.com',
        'ap-southeast-1': 'moderation.ap-southeast-1.myhuaweicloud.com'
    }
};

global.region = "cn-north-1";

module.exports = {

    /**
     * 将文件内容转化成base64的字符串
     * @param filepath 文件路径
     * @returns {string|*}
     */
    changeFileToBase64: function (filepath) {
        var filepath = path.resolve(filepath);
        var data = fs.readFileSync(filepath);
        return new Buffer(data).toString('base64');
    },

    /**
     *  ak，sk方式封装请求信息
     * @param sign 签名对象
     * @param request 请求对象
     * @param host 域名信息
     * @param method 请求类型
     * @param uri 请求路径
     * @param query get请求参数
     * @param headers 请求头信息
     * @param body 请求体信息
     */
    getHttpRequestEntity: function (sig, request, host, method, uri, query, headers, body) {
        request.rejectunauthorized = false;
        request.host = host;
        request.method = method;
        request.uri = uri;
        if (query !== "") {
            request.query = query;
        }
        request.headers = headers;
        if (body !== "") {
            request.body = JSON.stringify(body);
        } else {
            request.body = "";
        }
        var options = sig.Sign(request);
        return options;
    },
    /**
     * Token方式封装请求信息
     * @param host 域名信息
     * @param method 请求类型
     * @param uri 请求路径
     * @param headers 请求头信息
     * @returns {{host: *, method: *, path: *, rejectunauthorized: boolean, headers: *}}
     */
    getHttpRequestEntityOptions: function (host, method, uri, headers) {
        var options = {
            host: host,
            method: method,
            path: uri,
            rejectunauthorized: false,
            headers: headers
        };
        return options;
    },

    /**
     * Token方式封装GET请求信息
     * @param host 域名信息
     * @param method 请求类型
     * @param uri 请求路径
     * @param headers 请求头信息
     * @param queryString
     * @returns {{host: *, method: *, path: string, rejectunauthorized: boolean, headers: *}}
     */
    getHttpRequestEntityForGet: function (host, method, uri, headers, queryString) {
        var str = this.canonicalQueryString(queryString);
        if (str !== "") {
            str = "?" + str
        }
        var options = {
            host: host,
            method: method,
            path: encodeURI(uri) + str,
            rejectunauthorized: false,
            headers: headers
        };
        return options;
    },

    /**
     *  拼接GET请求参数的json对象
     * @param query 拼接对象
     * @returns {string} get请求的信息`
     * @constructor
     */
    canonicalQueryString: function (query) {
        var a = [];
        for (var key in query) {
            var value = query[key];
            var kv;
            if (value === '') {
                kv = signer.urlEncode(key)
            } else {
                kv = signer.urlEncode(key) + '=' + signer.urlEncode(value)
            }
            a.push(kv)
        }
        a.sort();
        return a.join('&');
    },
    /**
     *  构建获取token的请求体
     * @param username       用户名
     * @param domainname     账户名
     * @param password       密码
     * @param regionName     区域名
     */
    getRequestBodyForToken: function (username, password, domainName) {
        var param = {
            "auth": {
                "identity": {
                    "password": {
                        "user": {
                            "password": password,
                            "domain": {
                                "name": domainName
                            },
                            "name": username
                        }
                    },
                    "methods": [
                        "password"
                    ]
                },
                "scope": {
                    "project": {
                        "name": region
                    }
                }
            }
        };

        return JSON.stringify(param);
    },
    getFileByBase64Str: function (filePosition, base64Str) {
        var filePath = path.normalize(filePosition);
        fs.writeFileSync(filePath, new Buffer(base64Str, 'base64'));
        console.log(filePath);
    },

    /**
     * 初始化当前服务的region信息
     * @param region
     */
    initRegion : function (regionName) {
        region = regionName;
    },

    /**
     * 获取当前服务的请求域名
     * @param type
     */
    getEndPoint : function (type) {
        return ENDPOINT[type][region];
    }

};
