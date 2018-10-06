# -*- coding:utf-8 -*-

import urllib2
import json
import ssl
from gettoken import get_token
from utils import encode_to_base64, download_url_base64
from urllib2 import HTTPError, URLError

def request_moderation_url(token, inner_path, image_str=None, url=None):
    _url = 'https://ais.cn-north-1.myhuaweicloud.com' + inner_path

    _data = {
      "image":image_str,
      "url":url
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

#
# access moderation, image anti-porn
#
def image_antiporn(token, image_str=None, url=None ):
    print request_moderation_url(token, '/v1.1/moderation/image/anti-porn', image_str, url)
    print request_moderation_url(token, '/v1.0/moderation/image', image_str, url)

if __name__ == '__main__':
    user_name = '*******'
    password = '*******'
    account_name = '*******'  # the same as user_name in commonly use
    token = get_token(user_name, password, account_name)

    # call interface use the url download
    image_antiporn(token, download_url_base64('http://moderation-demo.ei.huaweicloud.com/theme/images/imagga/image_tagging_04.jpg'))

    # call interface use the url
    demo_data_url = 'https://ais-sample-data.obs.myhwclouds.com/tagging-normal.jpg'
    image_antiporn(token, '', demo_data_url )

    # call interface use the file
    image_antiporn(token, encode_to_base64('data/tagging-normal.jpg'))

