# -*- coding:utf-8 -*-

import sys
import json
import ais_sdk.ais as ais
import ais_sdk.utils as utils
import ais_sdk.signer as signer

#
# access image defog,post data by token
#
def image_defog(token, image, gamama=1.5):
    endpoint = utils.get_endpoint(ais.AisService.IMAGE_SERVICE)
    _url = 'https://%s/v1.0/vision/defog' % endpoint

    if sys.version_info.major >= 3:
        if image != '':
            image = image.decode("utf-8")

    _data = {
        "image": image,
        "gamma": gamama
    }

    status_code, resp = utils.request_token(_url, _data, token)
    if sys.version_info.major < 3:
        return resp
    else:
        return resp.decode('utf-8')


#
# access image defog,post data by ak,sk
#
def image_defog_aksk(_ak, _sk, image, gamama=1.5):
    endpoint = utils.get_endpoint(ais.AisService.IMAGE_SERVICE)
    _url = 'https://%s/v1.0/vision/defog' % endpoint

    sig = signer.Signer()
    sig.AppKey = _ak
    sig.AppSecret = _sk

    if sys.version_info.major >= 3:
        if image != '':
            image = image.decode("utf-8")

    _data = {
        "image": image,
        "gamma": gamama
    }

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = endpoint
    kreq.uri = "/v1.0/vision/defog"
    kreq.method = "POST"
    kreq.headers = {"Content-Type": "application/json"}
    kreq.body = json.dumps(_data)

    status_code, resp = utils.request_aksk(sig, kreq, _url)
    if sys.version_info.major < 3:
        return resp
    else:
        return resp.decode('utf-8')
