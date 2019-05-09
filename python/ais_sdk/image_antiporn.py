# -*- coding:utf-8 -*-

import sys
import json
import ais_sdk.ais as ais
import ais_sdk.utils as utils
import ais_sdk.signer as signer

def request_moderation_url(token, inner_path, image_str=None, url=None):
    endpoint = utils.get_endpoint(ais.AisService.MODERATION_SERVICE)
    _url = 'https://' + endpoint + inner_path

    if sys.version_info.major >= 3:
        if image_str != '':
            image_str = image_str.decode("utf-8")

    _data = {
        "image": image_str,
        "url": url
    }

    status_code, resp = utils.request_token(_url, _data, token)
    if sys.version_info.major < 3:
        return resp
    else:
        return resp.decode('utf-8')


#
# access moderation, image anti-porn,post data by token
#
def image_antiporn(token, image_str=None, url=None):
    print(request_moderation_url(token, '/v1.1/moderation/image/anti-porn', image_str, url))
    print(request_moderation_url(token, '/v1.0/moderation/image', image_str, url))


def request_moderation_url_aksk(sig, inner_path, image_str=None, url=None):
    endpoint = utils.get_endpoint(ais.AisService.MODERATION_SERVICE)
    _url = 'https://' + endpoint + inner_path

    if sys.version_info.major >= 3:
        if image_str != '':
            image_str = image_str.decode("utf-8")

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

    status_code, resp = utils.request_aksk(sig, kreq, _url)
    if sys.version_info.major < 3:
        return resp
    else:
        return resp.decode('utf-8')

#
# access moderation, image anti-porn,post data by ak,sk
#
def image_antiporn_aksk(_ak, _sk, image_str=None, url=None):
    sig = signer.Signer()
    sig.AppKey = _ak
    sig.AppSecret = _sk
    print (request_moderation_url_aksk(sig, '/v1.1/moderation/image/anti-porn', image_str, url))
    print (request_moderation_url_aksk(sig, '/v1.0/moderation/image', image_str, url))
