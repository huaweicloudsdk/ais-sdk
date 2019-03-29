# -*- coding:utf-8 -*-

import urllib2
import json
import ssl
from urllib2 import HTTPError, URLError
import signer
import ais
from utils import get_region_endponit


#
# access asr, asr_bgm,post data by token
#
def asr_bgm(region_name, token, url):
    endponit = get_region_endponit(ais.AisService.IMAGE_SERVICE, region_name)
    _url = 'https://%s/v1.0/bgm/recognition' % endponit

    _data = {
        "url": url,
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
# access asr, asr_bgm,post data by ak,sk
#
def asr_bgm_aksk(region_name, _ak, _sk, url):
    endponit = get_region_endponit(ais.AisService.IMAGE_SERVICE, region_name)
    _url = 'https://%s/v1.0/bgm/recognition' % endponit

    sig = signer.Signer()
    sig.AppKey = _ak
    sig.AppSecret = _sk
    _data = {
        "url": url,
    }

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = endponit
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
        req = urllib2.Request(url=_url, data=kreq.body, headers=kreq.headers)
        resp = urllib2.urlopen(req, context=_context)
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

    return resp.read().decode('unicode-escape').encode('utf-8')
