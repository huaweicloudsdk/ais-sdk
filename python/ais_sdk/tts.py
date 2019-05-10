# -*- coding:utf-8 -*-

import sys
import json
import ais_sdk.ais as ais
import ais_sdk.utils as utils
import ais_sdk.signer as signer

#
# access ocr vat invoice,post data by token
#
def tts(token, text, voice_name='xiaoyan', volume='0', sample_rate='16k', speech_speed='0', pitch_rate='0'):
    _url = 'https://%s/v1.0/voice/tts' % ais.AisEndpoint.TTS_ENDPOINT

    _data = {
        "text": text,
        "voice_name": voice_name,
        "volume": volume,
        "sample_rate": sample_rate,
        "speech_speed": speech_speed,
        "pitch_rate": pitch_rate
    }

    status_code, resp = utils.request_token(_url, _data, token)
    if sys.version_info.major < 3:
        return resp
    else:
        return resp.decode('utf-8')


#
# access ocr vat invoice,post data by ak,sk
#
def tts_aksk(_ak, _sk, text, voice_name='xiaoyan', volume='0', sample_rate='16k', speech_speed='0', pitch_rate='0'):
    _url = 'https://%s/v1.0/voice/tts' % ais.AisEndpoint.TTS_ENDPOINT

    sig = signer.Signer()
    sig.AppKey = _ak
    sig.AppSecret = _sk

    _data = {
        "text": text,
        "voice_name": voice_name,
        "volume": volume,
        "sample_rate": sample_rate,
        "speech_speed": speech_speed,
        "pitch_rate": pitch_rate
    }

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = ais.AisEndpoint.TTS_ENDPOINT
    kreq.uri = "/v1.0/voice/tts"
    kreq.method = "POST"
    kreq.headers = {"Content-Type": "application/json"}
    kreq.body = json.dumps(_data)

    status_code, resp = utils.request_aksk(sig, kreq, _url)
    if sys.version_info.major < 3:
        return resp
    else:
        return resp.decode('utf-8')