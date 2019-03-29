# -*- coding:utf-8 -*-

import urllib2
import json
import ssl
from urllib2 import HTTPError, URLError
import signer
import ais
from utils import get_region_endponit


#
# access moderation text enhance,posy data by token
#
def moderation_text(region_name, token, text, type='content',
                    categories=["ad", "porn", "abuse", "politics", "contraband", "flood"]):
    endponit = get_region_endponit(ais.AisService.MODERATION_SERVICE, region_name)
    _url = 'https://%s/v1.0/moderation/text' % endponit

    _data = {
        "categories": categories,  # 检测场景 Array politics：涉政 porn：涉黄 ad：广告 abuse：辱骂 contraband：违禁品 flood：灌水
        "items": [
            {"text": text, "type": type}  # items: 待检测的文本列表  text 待检测文本 type 文本类型
        ]
    }
    kreq = urllib2.Request(url=_url)
    kreq.add_header('Content-Type', 'application/json')
    kreq.add_header('X-Auth-Token', token)
    kreq.add_data(json.dumps(_data))

    resp = None
    status_code = None
    try:
        #
        # Here we use the unvertified-ssl-context, Because in FunctionStage
        # the client CA-validation have some problem, so we must do this.
        #
        _context = ssl._create_unverified_context()
        r = urllib2.urlopen(kreq, context=_context)

    #
    # We use HTTPError and URLError，because urllib2 can't process the 4XX &
    # 500 error in the single urlopen function.
    #
    # If you use a modern, high-level designed HTTP client lib, Yeah, I mean requests,
    # there is no this problem.
    #
    except HTTPError, e:
        resp = e.read()
        status_code = e.code
    except URLError, e:
        resp = e.read()
        status_code = e.code
    else:
        status_code = r.code
        resp = r.read()
    return resp.decode('unicode-escape').encode('utf-8')


#
# access moderation text enhance,posy data by ak,sk
#
def moderation_text_aksk(region_name, _ak, _sk, text, type='content',
                         categories=["ad", "porn", "abuse", "politics", "contraband", "flood"]):
    endponit = get_region_endponit(ais.AisService.MODERATION_SERVICE, region_name)
    _url = 'https://%s/v1.0/moderation/text' % endponit

    sig = signer.Signer()
    sig.AppKey = _ak
    sig.AppSecret = _sk

    _data = {
        "categories": categories,  # 检测场景 Array politics：涉政 porn：涉黄 ad：广告 abuse：辱骂 contraband：违禁品 flood：灌水
        "items": [
            {"text": text, "type": type}  # items: 待检测的文本列表  text 待检测文本 type 文本类型
        ]
    }

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = endponit
    kreq.uri = "/v1.0/moderation/text"
    kreq.method = "POST"
    kreq.headers = {"Content-Type": "application/json"}
    kreq.body = json.dumps(_data)

    resp = None
    status_code = None
    try:
        sig.Sign(kreq)
        #
        # Here we use the unvertified-ssl-context, Because in FunctionStage
        # the client CA-validation have some problem, so we must do this.
        #
        _context = ssl._create_unverified_context()
        req = urllib2.Request(url=_url, data=kreq.body, headers=kreq.headers)
        r = urllib2.urlopen(req, context=_context)
    #
    # We use HTTPError and URLError，because urllib2 can't process the 4XX &
    # 500 error in the single urlopen function.
    #
    # If you use a modern, high-level designed HTTP client lib, Yeah, I mean requests,
    # there is no this problem.
    #
    except HTTPError, e:
        resp = e.read()
        status_code = e.code
    except URLError, e:
        resp = e.read()
        status_code = e.code
    else:
        status_code = r.code
        resp = r.read()
    return resp.decode('unicode-escape').encode('utf-8')
