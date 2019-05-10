# -*- coding:utf-8 -*-

import sys
import json
import ais_sdk.ais as ais
import ais_sdk.utils as utils
import ais_sdk.signer as signer

#
# access moderation image content of batch,post data by token
#
def image_content_batch(token, urls, categories=None, threshold=None):
    endpoint = utils.get_endpoint(ais.AisService.MODERATION_SERVICE)
    _url = 'https://%s/v1.0/moderation/image/batch' % endpoint

    _data = {
        "urls": urls,
        "categories": categories,
        "threshold": threshold,
    }

    status_code, resp = utils.request_token(_url, _data, token)
    if sys.version_info.major < 3:
        return resp
    else:
        return resp.decode('utf-8')


#
# access moderation image content of batch,post data by token
#
def image_content_batch_aksk(_ak, _sk, urls, categories=None, threshold=None):
    endpoint = utils.get_endpoint(ais.AisService.MODERATION_SERVICE)
    _url = 'https://%s/v1.0/moderation/image/batch' % endpoint

    sig = signer.Signer()
    sig.AppKey = _ak
    sig.AppSecret = _sk

    _data = {
        "urls": urls,
        "categories": categories,
        "threshold": threshold,
    }

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = endpoint
    kreq.uri = "/v1.0/moderation/image/batch"
    kreq.method = "POST"
    kreq.headers = {"Content-Type": "application/json"}
    kreq.body = json.dumps(_data)

    status_code, resp = utils.request_aksk(sig, kreq, _url)
    if sys.version_info.major < 3:
        return resp
    else:
        return resp.decode('utf-8')
