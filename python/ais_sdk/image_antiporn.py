# -*- coding:utf-8 -*-

import urllib2
import json
import ssl
from urllib2 import HTTPError, URLError
import signer
import ais


def request_moderation_url(token, inner_path, image_str=None, url=None):
    _url = 'https://' + ais.AisEndpoint.ENDPOINT + inner_path

    _data = {
        "image": image_str,
        "url": url
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


#
# access moderation, image anti-porn,post data by token
#
def image_antiporn(token, image_str=None, url=None):
    print request_moderation_url(token, '/v1.1/moderation/image/anti-porn', image_str, url)
    print request_moderation_url(token, '/v1.0/moderation/image', image_str, url)


def request_moderation_url_aksk(sig, inner_path, image_str=None, url=None):
    _url = 'https://' + ais.AisEndpoint.ENDPOINT + inner_path

    _data = {
        "image": image_str,
        "url": url
    }

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = "ais.cn-north-1.myhuaweicloud.com"
    kreq.uri = inner_path
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
    except URLError, e:
        resp = e.read()
    else:
        resp = r.read()
    return resp


#
# access moderation, image anti-porn,post data by ak,sk
#
def image_antiporn_aksk(_ak, _sk, image_str=None, url=None):
    sig = signer.Signer()
    sig.AppKey = _ak
    sig.AppSecret = _sk
    print request_moderation_url_aksk(sig, '/v1.1/moderation/image/anti-porn', image_str, url)
    print request_moderation_url_aksk(sig, '/v1.0/moderation/image', image_str, url)
