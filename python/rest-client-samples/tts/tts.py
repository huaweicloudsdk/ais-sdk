# -*- coding:utf-8 -*-

import urllib2
import json
import ssl
from utils import decode_to_wave_file
from gettoken import get_token
from urllib2 import HTTPError, URLError

#
# access ocr vat invoice
#
def tts(token, text, voice_name = 'xiaoyan', volume='0', sample_rate = '16k', speech_speed = '0', pitch_rate = '0' ):
    _url = 'https://ais.cn-north-1.myhuaweicloud.com/v1.0/voice/tts'

    _data = {
        "text": text,
        "voice_name": voice_name,
        "volume": volume,
        "sample_rate": sample_rate,
        "speech_speed": speech_speed,
        "pitch_rate": pitch_rate
    }

    kreq = urllib2.Request( url = _url)
    kreq.add_header('Content-Type', 'application/json')
    kreq.add_header('X-Auth-Token', token )
    kreq.add_data(json.dumps(_data))
    
    resp = None
    status_code = None
    try:
        # 
        # Here we use the unvertified-ssl-context, Because in FunctionStage
        # the client CA-validation have some problem, so we must do this.
        #
        _context = ssl._create_unverified_context()
        r = urllib2.urlopen(kreq, context=_context)
        
    #
    # We use HTTPError and URLError，because urllib2 can't process the 4XX & 
    # 500 error in the single urlopen function.
    #
    # If you use a modern, high-level designed HTTP client lib, Yeah, I mean requests, 
    # there is no this problem. 
    #
    except HTTPError, e:
        resp = e.read()
        status_code = e.code
    except URLError, e:
        resp = e.read()
        status_code = e.code
    else:
        status_code = r.code
        resp = r.read()        
    return resp
if __name__ == '__main__':
    user_name = '*****'
    password = '******'
    account_name = '*****'  # the same as user_name in commonly use

    token = get_token(user_name, password, account_name)

    # call interface use the default config
    result = tts(token, '语音合成为你的业务增加交互的能力.')
    result_obj = json.loads(result)
    decode_to_wave_file(result_obj['result']['data'], 'data/tts_use_default_config.wav')

    # call interface use the specific config
    result = tts(token, '这里是语音合成的测试。', 'xiaoyu', '0', '16k')
    result_obj = json.loads(result)
    decode_to_wave_file(result_obj['result']['data'], 'data/tts_use_specific_config.wav')
