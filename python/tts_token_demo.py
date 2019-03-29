# -*- coding:utf-8 -*-

from ais_sdk.gettoken import get_token
from ais_sdk.utils import decode_to_wave_file
from ais_sdk.tts import tts

import json

if __name__ == '__main__':
    #
    # access text to speech,post data by token
    #
    user_name = '*****'
    password = '******'
    account_name = '*****'  # the same as user_name in commonly use
    region_name = '******'

    token = get_token(user_name, password, account_name, region_name)

    # call interface use the default config
    result = tts(token, '语音合成为你的业务增加交互的能力.')
    result_obj = json.loads(result)
    decode_to_wave_file(result_obj['result']['data'], 'data/tts_use_default_config.wav')

    # call interface use the specific config
    result = tts(token, '这里是语音合成的测试。', 'xiaoyu', '0', '16k')
    result_obj = json.loads(result)
    decode_to_wave_file(result_obj['result']['data'], 'data/tts_use_specific_config.wav')