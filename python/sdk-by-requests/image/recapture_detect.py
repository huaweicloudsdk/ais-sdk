#!/usr/bin/python
# -*- coding: utf-8 -*-

import requests
import utils
import authenticate
import ais

def recature_detect(filename, token, proxy_dict=None):
    '''
    process the recature detect
    :param image_base64_str:
    :return:
    '''
    image_base64_str = utils.encode_to_base64(filename)

    _payload = {"image": image_base64_str,
               "threshold": 0.99,
               "scene": ["recapture"]
               }
    _url = "%s/v1.0/image/recapture-detect" % ais.ENDPOINT
    _headers = {'Content-Type': 'application/json',
                'X-Auth-Token': token}

    if proxy_dict is None:
        r = requests.post(_url, json=_payload, headers=_headers)
    else:
        r = requests.post(_url, json=_payload, headers=_headers, proxies=proxy_dict)
    return r.json()


def recature_detect_with_proxy(filename, token, proxy_host):
    """
    tagging with proxy
    :return:
    """

    _proxy_dict = {
        "https": proxy_host
    }

    return recature_detect(filename, token, _proxy_dict)

if __name__ == '__main__':
    #
    # proxy host config
    #
    proxy_user = "*****"
    proxy_password = "*****"
    https_proxy = "https://%s:%s@proxycn2.example.com:8080" % (proxy_user, proxy_password)

    #
    # user/password config
    #
    user_name = "*****"
    password = "*****"

    #
    # get the token
    #
    token = authenticate.get_token_with_proxy(user_name, password, https_proxy)

    #
    # processing with one file
    #
    print recature_detect_with_proxy(u'pic/demo.jpg', token, https_proxy)

    #
    # callback function for batch processing
    #
    def process_callback(filename):
        global https_proxy
        global token
        data = recature_detect_with_proxy(filename, token, https_proxy)
        s = ""
        s += "%s, %s, %s " % (data["result"]["suggestion"], data["result"]["category"], data["result"]["score"])
        for e in data["result"]["detail"]:
            s += "%s, %s; " % (e["label"], e["confidence"])
        print "%s ===== %s " % (filename, s)

    #
    # processing with batch file
    #
    import sys
    if len(sys.argv) == 2:
        rootdir = sys.argv[1]
        utils.batch_processing(rootdir, process_callback, 1.0)
    else:
        utils.batch_processing(".", process_callback, 1.0)