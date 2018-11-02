# -*- coding:utf-8 -*-
import ssl
import signer
from urllib.error import URLError, HTTPError
import urllib.parse
import urllib.request
import json

#
# access asr, asr_sentence,post data by token
#
def asr_sentence(token, data, url, encode_type='wav', sample_rate='8k'):
    _url = 'https://ais.cn-north-1.myhuaweicloud.com/v1.0/voice/asr/sentence'

    if data != '':
        data = data.decode("utf-8")

    _data = {
      "url":url,
      "data": data,
      "encode_type": encode_type,
      "sample_rate": sample_rate
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
        r = urllib.request.urlopen(kreq,context=_context)
        
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
# access asr, asr_sentence,post data by ak,sk
#
def asr_sentence_aksk(_ak, _sk, data, url, encode_type='wav', sample_rate='8k'):
    _url = "https://ais.cn-north-1.myhuaweicloud.com/v1.0/voice/asr/sentence"

    sig = signer.Signer()
    sig.AppKey = _ak
    sig.AppSecret = _sk

    if data != '':
        data = data.decode("utf-8")

    _data = {
        "url": url,
        "data": data,
        "encode_type": encode_type,
        "sample_rate": sample_rate
    }

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = "ais.cn-north-1.myhuaweicloud.com"
    kreq.uri = "/v1.0/voice/asr/sentence"
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
        req = urllib.request.Request(url=_url, data=bytes(kreq.body), headers=kreq.headers)
        r = urllib.request.urlopen(req,context=_context)
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
    return resp.decode('utf-8')


