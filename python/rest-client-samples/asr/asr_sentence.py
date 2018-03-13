# -*- coding:utf-8 -*-

import urllib2
import json
import ssl
from gettoken import get_token
from utils import encode_to_base64
from urllib2 import HTTPError, URLError

#
# access moderation, image anti-porn
#
def asr_sentence(token, data, url, encode_type='wav', sample_rate='8k'):
    _url = 'https://ais.cn-north-1.myhuaweicloud.com/v1.0/voice/asr/sentence'

    _data = {
      "url":url,
      "data": data,
      "encode_type": encode_type,
      "sample_rate": sample_rate
    }

    kreq = urllib2.Request( url = _url)
    kreq.add_header('Content-Type', 'application/json')
    kreq.add_header('X-Auth-Token', token )
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

if __name__ == '__main__':
    user_name = '******'
    password = '******'
    account_name = '******'  # the same as user_name in commonly use
    demo_data_url = 'https://ais-sample-data.obs.myhwclouds.com/asr-sentence.wav'
    token = get_token(user_name, password, account_name)

    # call interface use the url
    result = asr_sentence(token, '', demo_data_url, 'wav', '16k')
    print result

    # call interface use the file
    result = asr_sentence(token, encode_to_base64('data/asr-sentence.wav'), '', 'wav', '16k')
    print result
