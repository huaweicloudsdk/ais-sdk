# -*- coding:utf-8 -*-

import urllib2
import json
import ssl
from urllib2 import HTTPError, URLError

#
# access image dark enhance
#
def moderation_text(token, text, type='content', cattegories=[ "ad","politics","politics","politics",  "contraband",  "contraband"]):
    _url = 'https://ais.cn-north-1.myhuaweicloud.com/v1.0/moderation/text'

    _data = {
        "categories": cattegories,   # 检测场景 Array politics：涉政 porn：涉黄 ad：广告 abuse：辱骂 contraband：违禁品 flood：灌水
        "items": [
            {  "text": text,"type": type }    #items: 待检测的文本列表  text 待检测文本 type 文本类型
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
    return resp