# -*- coding:utf-8 -*-

import sys
import json
import ais_sdk.ais as ais
import ais_sdk.utils as utils
import ais_sdk.signer as signer

#
# access moderation text enhance,posy data by token
#
def moderation_text(token, text, type='content',
                    categories=["ad", "politics", "porn", "abuse", "contraband", "flood"]):
    endpoint = utils.get_endpoint(ais.AisService.MODERATION_SERVICE)
    _url = 'https://%s/v1.0/moderation/text' % endpoint

    _data = {
        "categories": categories,  # 检测场景 Array politics：涉政 porn：涉黄 ad：广告 abuse：辱骂 contraband：违禁品 flood：灌水
        "items": [
            {"text": text, "type": type}  # items: 待检测的文本列表  text 待检测文本 type 文本类型
        ]
    }

    status_code, resp = utils.request_token(_url, _data, token)
    if sys.version_info.major < 3:
        return resp.decode('unicode-escape').encode('utf-8')
    else:
        return resp.decode('unicode_escape')


#
# access moderation text enhance,posy data by ak,sk
#
def moderation_text_aksk(_ak, _sk, text, type='content',
                         categories=["ad", "politics", "porn", "abuse", "contraband", "flood"]):
    endpoint = utils.get_endpoint(ais.AisService.MODERATION_SERVICE)
    _url = 'https://%s/v1.0/moderation/text' % endpoint

    sig = signer.Signer()
    sig.AppKey = _ak
    sig.AppSecret = _sk

    _data = {
        "categories": categories,  # 检测场景 Array politics：涉政 porn：涉黄 ad：广告 abuse：辱骂 contraband：违禁品 flood：灌水
        "items": [
            {"text": text, "type": type}  # items: 待检测的文本列表  text 待检测文本 type 文本类型
        ]
    }

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = endpoint
    kreq.uri = "/v1.0/moderation/text"
    kreq.method = "POST"
    kreq.headers = {"Content-Type": "application/json"}
    kreq.body = json.dumps(_data)

    status_code, resp = utils.request_aksk(sig, kreq, _url)
    if sys.version_info.major < 3:
        return resp.decode('unicode-escape').encode('utf-8')
    else:
        return resp.decode('unicode_escape')
