# -*- coding:utf-8 -*-
import ssl
import ais_sdk.signer as signer
from urllib.error import URLError, HTTPError
import urllib.parse
import urllib.request
import json
import ais_sdk.ais as ais
import ais_sdk.utils as utils

def request_moderation_url(token, inner_path, image_str=None, url=None):
    endpoint = utils.get_endpoint(ais.AisService.MODERATION_SERVICE)
    _url = 'https://%s' % (endpoint + inner_path)

    if image_str != '':
        image_str = image_str.decode("utf-8")

    _data = {
        "image": image_str,
        "url": url
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
    return resp.decode('utf-8')


#
# access moderation, image anti-porn,post data by token
#
def image_antiporn(token, image_str=None, url=None):
    print(request_moderation_url(token, '/v1.1/moderation/image/anti-porn', image_str, url))
    print(request_moderation_url(token, '/v1.0/moderation/image', image_str, url))


def request_moderation_url_aksk(sig, inner_path, image_str=None, url=None):
    endpoint = utils.get_endpoint(ais.AisService.MODERATION_SERVICE)
    _url = 'https://%s' % (endpoint + inner_path)

    if image_str != '':
        image_str = image_str.decode('utf-8')

    _data = {
        "image": image_str,
        "url": url
    }

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = endpoint
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
    return resp.decode('utf-8')


#
# access moderation, image anti-porn,post data by ak,sk
#
def image_antiporn_aksk(_ak, _sk, image_str=None, url=None):
    sig = signer.Signer()
    sig.AppKey = _ak
    sig.AppSecret = _sk
    print(request_moderation_url_aksk(sig, '/v1.1/moderation/image/anti-porn', image_str, url))
    print(request_moderation_url_aksk(sig, '/v1.0/moderation/image', image_str, url))
