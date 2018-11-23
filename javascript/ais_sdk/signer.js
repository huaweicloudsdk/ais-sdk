// HWS API Gateway Signature
(function (root, factory) {
    "use strict";

    /*global define*/
    if (typeof define === 'function' && define.amd) {
        // AMD
        define(['CryptoJS', 'moment-timezone'], function (CryptoJS, moment) {
            var crypto_wrapper = {
                hmacsha256: function (keyByte, message) {
                    return CryptoJS.HmacSHA256(message, keyByte).toString(CryptoJS.enc.Hex)
                },
                HexEncodeSHA256Hash: function (body) {
                    return CryptoJS.SHA256(body)
                }
            };
            return factory(crypto_wrapper, moment)
        });
    }
    else if (typeof module === 'object' && module.exports) {
        // Node
        var crypto = require('crypto');
        var crypto_wrapper = {
            hmacsha256: function (keyByte, message) {
                return crypto.createHmac('SHA256', keyByte).update(message).digest().toString('hex')
            },
            HexEncodeSHA256Hash: function (body) {
                return crypto.createHash('SHA256').update(body).digest().toString('hex')
            }
        };
        module.exports = factory(crypto_wrapper, require('moment-timezone'));
    }
    else {
        // Browser
        var CryptoJS = root.CryptoJS
        var crypto_wrapper = {
            hmacsha256: function (keyByte, message) {
                return CryptoJS.HmacSHA256(message, keyByte).toString(CryptoJS.enc.Hex)
            },
            HexEncodeSHA256Hash: function (body) {
                return CryptoJS.SHA256(body)
            }
        };
        root.signer = factory(crypto_wrapper, root.moment);
    }
}(this, function (crypto_wrapper, moment) {
    'use strict';

    var BasicDateFormat = "YYYYMMDDTHHmmss[Z]";
    var Algorithm = "SDK-HMAC-SHA256";
    var HeaderXDate = "X-Sdk-Date";
    var HeaderAuthorization = "Authorization";
    var HeaderContentSha256 = "x-sdk-content-sha256";

    const hexTable = new Array(256);
    for (var i = 0; i < 256; ++i)
        hexTable[i] = '%' + ((i < 16 ? '0' : '') + i.toString(16)).toUpperCase();

    const noEscape = [
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 0 - 15
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 16 - 31
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, // 32 - 47
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, // 48 - 63
        0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, // 64 - 79
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, // 80 - 95
        0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, // 96 - 111
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0  // 112 - 127
    ];

    // function urlEncode is based on https://github.com/nodejs/node/blob/master/lib/querystring.js
    // Copyright Joyent, Inc. and other Node contributors.
    function urlEncode(str) {
        if (typeof str !== 'string') {
            if (typeof str === 'object')
                str = String(str);
            else
                str += '';
        }
        var out = '';
        var lastPos = 0;

        for (var i = 0; i < str.length; ++i) {
            var c = str.charCodeAt(i);

            // ASCII
            if (c < 0x80) {
                if (noEscape[c] === 1)
                    continue;
                if (lastPos < i)
                    out += str.slice(lastPos, i);
                lastPos = i + 1;
                out += hexTable[c];
                continue;
            }

            if (lastPos < i)
                out += str.slice(lastPos, i);

            // Multi-byte characters ...
            if (c < 0x800) {
                lastPos = i + 1;
                out += hexTable[0xC0 | (c >> 6)] + hexTable[0x80 | (c & 0x3F)];
                continue;
            }
            if (c < 0xD800 || c >= 0xE000) {
                lastPos = i + 1;
                out += hexTable[0xE0 | (c >> 12)] +
                    hexTable[0x80 | ((c >> 6) & 0x3F)] +
                    hexTable[0x80 | (c & 0x3F)];
                continue;
            }
            // Surrogate pair
            ++i;

            if (i >= str.length)
                throw new errors.URIError('ERR_INVALID_URI');

            var c2 = str.charCodeAt(i) & 0x3FF;

            lastPos = i + 1;
            c = 0x10000 + (((c & 0x3FF) << 10) | c2);
            out += hexTable[0xF0 | (c >> 18)] +
                hexTable[0x80 | ((c >> 12) & 0x3F)] +
                hexTable[0x80 | ((c >> 6) & 0x3F)] +
                hexTable[0x80 | (c & 0x3F)];
        }
        if (lastPos === 0)
            return str;
        if (lastPos < str.length)
            return out + str.slice(lastPos);
        return out;
    }

    function HttpRequest() {
        this.method = "";
        this.host = "";   //    example.com
        this.uri = "";     //    /request/uri
        this.query = {};
        this.headers = {};
        this.body = "";
    }


// Build a CanonicalRequest from a regular request string
//
// CanonicalRequest =
//  HTTPRequestMethod + '\n' +
//  CanonicalURI + '\n' +
//  CanonicalQueryString + '\n' +
//  CanonicalHeaders + '\n' +
//  SignedHeaders + '\n' +
//  HexEncode(Hash(RequestPayload))
    function CanonicalRequest(r) {
        var hexencode;
        if (r.headers[HeaderContentSha256] !== undefined) {
            hexencode = r.headers[HeaderContentSha256]
        } else {
            var data = RequestPayload(r);
            hexencode = crypto_wrapper.HexEncodeSHA256Hash(data);
        }
        return r.method + "\n" + CanonicalURI(r) + "\n" + CanonicalQueryString(r) + "\n" + CanonicalHeaders(r) + "\n" + SignedHeaders(r) + "\n" + hexencode
    }

    function CanonicalURI(r) {
        var pattens = r.uri.split('/');
        var uri = [];
        for (var k in pattens) {
            var v = pattens[k];
            uri.push(urlEncode(v))
        }
        var urlpath = uri.join('/');
        if (urlpath[urlpath.length - 1] !== '/') {
            urlpath = urlpath + '/'
        }
        //r.uri = urlpath
        return urlpath;
    }

    function CanonicalQueryString(r) {
        var a = [];
        for (var key in r.query) {
            var value = r.query[key];
            var kv;
            if (value === '') {
                kv = urlEncode(key)
            } else {
                kv = urlEncode(key) + '=' + urlEncode(value)
            }
            a.push(kv)
        }
        a.sort();
        return a.join('&');
    }

    function CanonicalHeaders(r) {
        var a = [];
        var headers = {};
        for (var key in r.headers) {
            var value = r.headers[key];
            var keyEncoded = key.toLowerCase();
            headers[keyEncoded] = value;
            a.push(keyEncoded + ':' + value.trim())
        }
        a.sort();
        r.headers = headers;
        return a.join('\n') + "\n"
    }

    function SignedHeaders(r) {
        var a = [];
        for (var key in r.headers) {
            a.push(key.toLowerCase())
        }
        a.sort();
        return a.join(';')
    }

    function RequestPayload(r) {
        return r.body
    }

// Create a "String to Sign".
    function StringToSign(canonicalRequest, t) {
        var bytes = crypto_wrapper.HexEncodeSHA256Hash(canonicalRequest);
        return Algorithm + "\n" + t.format(BasicDateFormat) + "\n" + bytes
    }

// Create the HWS Signature.
    function SignStringToSign(stringToSign, signingKey) {
        return crypto_wrapper.hmacsha256(signingKey, stringToSign)
    }

// Get the finalized value for the "Authorization" header.  The signature
// parameter is the output from SignStringToSign
    function AuthHeaderValue(signature, AppKey, signedHeaders) {
        return Algorithm + " Access=" + AppKey + ", SignedHeaders=" + signedHeaders + ", Signature=" + signature
    }

    function Signer() {
        this.AppKey = "";
        this.AppSecret = "";
    }

    Signer.prototype.Sign = function (r) {
        var headerTime = r.headers[HeaderXDate];
        var t;
        if (headerTime === undefined) {
            t = moment(Date.now()).tz('utc');
            r.headers[HeaderXDate] = t.format(BasicDateFormat)
        }
        else {
            t = moment(headerTime, BasicDateFormat)
        }
        if (r.method !== "PUT" && r.method !== "PATCH" && r.method !== "POST") {
            r.body = ""
        }
        var queryString = CanonicalQueryString(r);
        if (queryString !== "") {
            queryString = "?" + queryString
        }
        var options = {
            hostname: r.host,
            path: encodeURI(r.uri) + queryString,
            method: r.method,
            headers: r.headers
        };
        CanonicalHeaders(r);//transfer headers key to lower case
        if (r.headers.host === undefined) {
            r.headers.host = r.host;
        }
        var canonicalRequest = CanonicalRequest(r);
        var stringToSign = StringToSign(canonicalRequest, t);
        var signature = SignStringToSign(stringToSign, this.AppSecret);
        var signedHeaders = SignedHeaders(r);
        options.headers[HeaderAuthorization] = AuthHeaderValue(signature, this.AppKey, signedHeaders);
        return options
    };
    return {
        HttpRequest: HttpRequest,
        Signer: Signer,
        urlEncode: urlEncode
    }
}));
