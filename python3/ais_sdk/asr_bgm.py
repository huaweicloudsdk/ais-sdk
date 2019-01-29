# -*- coding:utf-8 -*-
import ssl
import ais_sdk.signer as signer
from urllib.error import URLError, HTTPError
import urllib.parse
import urllib.request
import json
import ais_sdk.ais as ais


#
# access asr, asr_bgm,post data by token
#
def asr_bgm(token, url):
    _url = 'https://%s/v1.0/bgm/recognition' % ais.AisEndpoint.IMAGE_ENDPOINT

    _data = {
        "url": url,
    }
    _headers = {
        "Content-Type": "application/json",
        "X-Auth-Token": token
    }

    data = bytes(json.dumps(_data), 'utf8')
    kreq = urllib.request.Request(_url, data, _headers)

    resp = None
    status_code = None
    try:
        #
        # Here we use the unvertified-ssl-context, Because in FunctionStage
        # the client CA-validation have some problem, so we must do this.
        #
        r = urllib.request.urlopen(kreq)

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
# access asr, asr_bgm,post data by ak,sk
#
def asr_bgm_aksk(_ak, _sk, url):
    _url = 'https://%s/v1.0/bgm/recognition' % ais.AisEndpoint.IMAGE_ENDPOINT

    sig = signer.Signer()
    sig.AppKey = _ak
    sig.AppSecret = _sk
    _data = {
        "url": url,
    }

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = ais.AisEndpoint.IMAGE_ENDPOINT
    kreq.uri = "/v1.0/bgm/recognition"
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
    # We use HTTPError and URLError，because urllib can't process the 4XX &
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
