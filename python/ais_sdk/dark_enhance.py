# -*- coding:utf-8 -*-

import sys
import json
import ais_sdk.ais as ais
import ais_sdk.utils as utils
import ais_sdk.signer as signer

#
# access image dark enhance,post data by token
#
def dark_enhance(token, image, brightness=0.9):
    endpoint = utils.get_endpoint(ais.AisService.IMAGE_SERVICE)
    _url = 'https://%s/v1.0/vision/dark-enhance' % endpoint

    if sys.version_info.major >= 3:
        if image != '':
            image = image.decode("utf-8")

    _data = {
        "image": image,
        "brightness": brightness
    }

    status_code, resp = utils.request_token(_url, _data, token)
    if sys.version_info.major < 3:
        return resp
    else:
        return resp.decode('utf-8')
#
# access image dark enhance by ak,sk
#
def dark_enhance_aksk(_ak, _sk, image, brightness=0.9):
    endpoint = utils.get_endpoint(ais.AisService.IMAGE_SERVICE)
    _url = 'https://%s/v1.0/vision/dark-enhance' % endpoint

    sig = signer.Signer()
    sig.AppKey = _ak
    sig.AppSecret = _sk

    if sys.version_info.major >= 3:
        if image != '':
            image = image.decode("utf-8")

    _data = {
        "image": image,
        "brightness": brightness
    }

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = endpoint
    kreq.uri = "/v1.0/vision/dark-enhance"
    kreq.method = "POST"
    kreq.headers = {"Content-Type": "application/json"}
    kreq.body = json.dumps(_data)

    status_code, resp = utils.request_aksk(sig, kreq, _url)
    if sys.version_info.major < 3:
        return resp
    else:
        return resp.decode('utf-8')
