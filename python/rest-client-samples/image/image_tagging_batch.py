# -*- coding:utf-8 -*-

import sys
import urllib2
import json
import ssl
import base64
from urllib2 import HTTPError, URLError
from gettoken import get_token

reload(sys)
sys.setdefaultencoding('utf8')

def download_url_base64(url):
    try:
        r = urllib2.urlopen(url)
    except HTTPError, e:
        resp = e.read()
        status_code = e.code
    except URLError, e:
        resp = e.read()
        status_code = e.code
    else:
        status_code = r.code
        resp = r.read()        

    if status_code != 200:
        print "Error get url ", url, status_code
        return ""
    return base64.b64encode(resp)

def image_tagging(token, image_base64):
    _url = 'https://ais.cn-north-1.myhuaweicloud.com/v1.0/image/tagging'

    _data = {
      "image": image_base64,
      "language": "zh",
      "limit": 10,
      "threshold": 10.0
    }

    kreq = urllib2.Request( url = _url)
    kreq.add_header('Content-Type', 'application/json')
    kreq.add_header('X-Auth-Token', token )
    kreq.add_data(json.dumps(_data))
    
    resp = None
    status_code = None
    try:
        r = urllib2.urlopen(kreq)
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

def url_image_tagging(token, url):
    image = download_url_base64(url)
    if len(image) == 0:
        print "%s\t%s" %(url, "ERRORdownload")
        return 

    resp = image_tagging(token, image)
    print "%s\t%s" %(url, resp)

if __name__ == "__main__":
    user_name = "XXX"
    password = "XXX"
    account_name = "XXX"

    url_file = sys.argv[1]

    # token expire in 24hour
    token = get_token(user_name, password, account_name)
    if len(token) == 0:
        print "Error username password"
        sys.exit(-1)

    # test urls in file
    for line in open(url_file):
        url = line.strip()
        url_image_tagging(token, url)

    ## test a sigle url
    #url_image_tagging(token, 'http://www.example.com/example.jpg')
