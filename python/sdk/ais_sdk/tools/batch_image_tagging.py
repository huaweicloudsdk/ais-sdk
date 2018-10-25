#!/usr/bin/python
# -*- coding: utf-8 -*-
 
import os
import sys
import base64
import time
reload(sys)
sys.setdefaultencoding('utf-8')
 
import requests
import traceback
 
def encode_to_base64(filename):
    """
    encoding file to base64 encoded stream text
    :param filename:
    :return:
    """
    file = open(filename, 'rb')
    imgstr = base64.b64encode(file.read())
    file.close()
    return imgstr
 
def do_tagging(rootdir, token, proxy_dict):
    """
    process image tagging in batch mode
    :param rootdir:
    :return:
    """
    print "Start tagging the path: %s ..." % rootdir
    for parent, dirnames, filenames in os.walk(rootdir):
        for filename in filenames:
            # only check the file name with the postfix "jpg, png, tiff"
            if filename.lower().endswith(".jpg") or filename.lower().endswith(".png") or filename == "all":
                filename = "%s/%s" % (parent, filename)
 
                # if has some error, then log the file name.
                try:
                    data = tagging_with_proxy(filename, token, proxy_dict)
                    filename = filename.decode("gb2312")
                    s = ""
                    for e in data["result"]["tags"]:
                        s += "%s, %s; " % (e["tag"], e["confidence"])
                    print "%s ===== %s " % (filename, s)
                    time.sleep(6.0)
                except Exception as e:
                    print "==================Find Error==================="
                    print "The file tagging failed, file name is: %s" % filename
                    print "exception is : %s %s" % (e, traceback.format_exc())
                    print data
 
    print "End tagging the path: %s ..." % rootdir
 
def tagging(filename, token, proxy_dict=None):
    '''
    process the image tagging
    :param image_base64_str:
    :return:
    '''
    image_base64_str = encode_to_base64(filename)
 
    payload = {"image": image_base64_str,
               "url": "",
               "threshold": "60",
               "language": "zh"
               }
    url = "https://ais.cn-north-1.myhuaweicloud.com/v1.0/image/tagging"
    _headers = {'Content-Type': 'application/json',
                'X-Auth-Token': token}
 
    if proxy_dict is None:
        r = requests.post(url, json=payload, headers=_headers)
    else:
        r = requests.post(url, json=payload, headers=_headers, proxies=proxy_dict)
    return r.json()
 
def tagging_with_proxy(filename, token, proxy_host):
    """
    tagging with proxy
    :return:
    """
 
    _proxy_dict = {
        "https": proxy_host
    }
 
    return tagging(filename, token, _proxy_dict)
 
def get_token(user_name, password, proxy_dict=None):
    """
    process get token for following processing
    :return:
    """
    _urltoken = "https://iam.cn-north-1.myhuaweicloud.com/v3/auth/tokens"
    _headers = {'Content-Type': 'application/json'}
    _paramstoken = {
        "auth": {
            "identity": {
                "methods": ["password"],
                "password": {
                    "user": {
                        "name": user_name,
                        "password": password,
                        "domain": {"name": user_name}}}},
            "scope": {
                "project": {"name": "cn-north-1"}}}
    }
    if proxy_dict is None:
        r = requests.post(_urltoken, json=_paramstoken, headers=_headers)
    else:
        r = requests.post(_urltoken, json=_paramstoken, headers=_headers, proxies=proxy_dict)
 
    return r.headers.get("X-Subject-Token")
 
def get_token_with_proxy(user_name, password, proxy_host):
    """
    get token with proxy
    :return:
    """
 
    _proxy_dict = {
        "https": proxy_host
    }
 
    return get_token(user_name, password, _proxy_dict)
 
if __name__ == '__main__':
 
    #
    # proxy host config
    #
    proxy_user = "******"
    proxy_password = "******!"
    https_proxy = "https://%s:%s@proxy.example.com:8080" % (proxy_user, proxy_password)
 
    #
    # user/password config
    #
    user_name = "******"
    password = "******"
    token = get_token_with_proxy(user_name, password, https_proxy)
 
    if len(sys.argv) == 2:
        rootdir = sys.argv[1]
        do_tagging(rootdir, token, https_proxy)
    else:
        do_tagging(".", token, https_proxy)
