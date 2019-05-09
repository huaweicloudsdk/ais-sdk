# -*- coding:utf-8 -*-

import sys
import json
import ais_sdk.ais as ais
import ais_sdk.utils as utils
import ais_sdk.signer as signer

#
# access asr, asr_sentence,post data by token
#
def asr_sentence(token, data, url, encode_type='wav', sample_rate='8k'):
    _url = 'https://%s/v1.0/voice/asr/sentence' % ais.AisEndpoint.ASR_ENDPOINT

    if sys.version_info.major >= 3:
        if data != '':
            data = data.decode("utf-8")

    _data = {
        "url": url,
        "data": data,
        "encode_type": encode_type,
        "sample_rate": sample_rate
    }

    status_code, resp = utils.request_token(_url, _data, token)
    if sys.version_info.major < 3:
        return resp.decode('unicode-escape').encode('utf-8')
    else:
        return resp.decode('unicode_escape')


#
# access asr, asr_sentence,post data by ak,sk
#
def asr_sentence_aksk(_ak, _sk, data, url, encode_type='wav', sample_rate='8k'):
    _url = "https://%s/v1.0/voice/asr/sentence" % ais.AisEndpoint.ASR_ENDPOINT

    sig = signer.Signer()
    sig.AppKey = _ak
    sig.AppSecret = _sk

    if sys.version_info.major >= 3:
        if data != '':
            data = data.decode("utf-8")

    _data = {
        "url": url,
        "data": data,
        "encode_type": encode_type,
        "sample_rate": sample_rate
    }

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = ais.AisEndpoint.ASR_ENDPOINT
    kreq.uri = "/v1.0/voice/asr/sentence"
    kreq.method = "POST"
    kreq.headers = {"Content-Type": "application/json"}
    kreq.body = json.dumps(_data)

    status_code, resp = utils.request_aksk(sig, kreq, _url)
    if sys.version_info.major < 3:
        return resp.decode('unicode-escape').encode('utf-8')
    else:
        return resp.decode('unicode_escape')
