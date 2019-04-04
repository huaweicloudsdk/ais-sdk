# -*- coding:utf-8 -*-

import ssl
import ais_sdk.signer as signer
from urllib.error import URLError, HTTPError
import urllib.parse
import urllib.request
import json
import ais_sdk.ais as ais
import ais_sdk.utils as utils

#
# access moderation text enhance,posy data by token
#
def moderation_text(token, text, type='content',
                    categories=["ad", "politics", "porn", "abuse", "contraband", "flood"]):
    endpoint = utils.get_endpoint(ais.AisService.MODERATION_SERVICE)
    _url = 'https://%s/v1.0/moderation/text' % endpoint

    _data = {
        "categories": categories,
        "items": [
            {"text": text, "type": type}
        ]
    }
    _headers = {
        "Content-Type": "application/json",
        "X-Auth-Token": token
    }

    data = json.dumps(_data).encode("utf-8")
    kreq = urllib.request.Request(_url, data, _headers)

    resp = None
    status_code = None
    try:
        #
        # Here we use the unvertified-ssl-context, Because in FunctionStage
        # the client CA-validation have some problem, so we must do this.
        #
        _context = ssl._create_unverified_context()
        r = urllib.request.urlopen(kreq, context=_context)

    #
    # We use HTTPError and URLError，because urllib2 can't process the 4XX &
    # 500 error in the single urlopen function.
    #
    # If you use a modern, high-level designed HTTP client lib, Yeah, I mean requests,
    # there is no this problem.
    #
    except HTTPError as e:
        resp = e.read()
        status_code = e.code
    except URLError as e:
        resp = e.read()
        status_code = e.code
    else:
        status_code = r.code
        resp = r.read()
    return resp.decode('unicode_escape')


#
# access moderation text enhance,posy data by ak,sk
#
def moderation_text_aksk(_ak, _sk, text, type='content',
                         categories=["ad", "politics", "porn", "abuse", "contraband", "flood"]):
    endpoint = utils.get_endpoint(ais.AisService.MODERATION_SERVICE)
    _url = 'https://%s/v1.0/moderation/text' % endpoint

    sig = signer.Signer()
    sig.AppKey = _ak
    sig.AppSecret = _sk

    _data = {
        "categories": categories,
        "items": [
            {"text": text, "type": type}
        ]
    }

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = endpoint
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
        req = urllib.request.Request(url=_url, data=kreq.body, headers=kreq.headers)
        r = urllib.request.urlopen(req, context=_context)
    #
    # We use HTTPError and URLError，because urllib2 can't process the 4XX &
    # 500 error in the single urlopen function.
    #
    # If you use a modern, high-level designed HTTP client lib, Yeah, I mean requests,
    # there is no this problem.
    #
    except HTTPError as e:
        resp = e.read()
        status_code = e.code
    except URLError as e:
        resp = e.read()
        status_code = e.code
    else:
        status_code = r.code
        resp = r.read()
    return resp.decode('unicode_escape')
