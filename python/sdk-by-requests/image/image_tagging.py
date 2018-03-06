#!/usr/bin/python
# -*- coding: utf-8 -*-

from core import ais
import sys
import requests

import core.authenticate

def tagging(filename, token, proxy_dict=None):
    '''
    process the image tagging
    :param image_base64_str:
    :return:
    '''
    image_base64_str = utils.encode_to_base64(filename)

    _payload = {"image": image_base64_str,
               "url": "",
               "threshold": "30",
               "language": "zh"
               }
    _url = "%s/v1.0/image/tagging" % ais.ENDPOINT
    _headers = {'Content-Type': 'application/json',
                'X-Auth-Token': token}

    if proxy_dict is None:
        r = requests.post(_url, json=_payload, headers=_headers)
    else:
        r = requests.post(_url, json=_payload, headers=_headers, proxies=proxy_dict)
    return r.json()


def tagging_with_proxy(filename, token, proxy_host):
    """
    process image tagging with proxy
    :return:
    """

    _proxy_dict = {
        "https": proxy_host
    }

    return tagging(filename, token, _proxy_dict)

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
    token = authenticate.get_token_with_proxy(user_name, password, https_proxy)

    data = tagging_with_proxy(u'pic/demo.jpg', token, https_proxy)

    #
    # callback function for batch processing
    #
    def process_callback(filename):
        global https_proxy
        global token
        data = tagging_with_proxy(filename, token, https_proxy)
        s = ""
        for e in data["result"]["tags"]:
            s += "%s, %s; " % (e["tag"], e["confidence"])
        print "%s ===== %s " % (filename, s)

    if len(sys.argv) == 2:
        rootdir = sys.argv[1]
        utils.batch_processing(rootdir, process_callback, 6.0)
    else:
        utils.batch_processing(".", process_callback, 6.0)
