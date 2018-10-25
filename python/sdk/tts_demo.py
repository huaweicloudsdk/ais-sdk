# -*- coding:utf-8 -*-

from core.gettoken import get_token
from core.utils import encode_to_base64, decode_to_wave_file
from tts.tts import tts
import json

if __name__ == '__main__':
    user_name = '*****'
    password = '******'
    account_name = '*****'  # the same as user_name in commonly use
    user_name = 'l65717'
    password = 'il0veais'
    account_name = 'l65717'  # the same as user_name in commonly use
    token = get_token(user_name, password, account_name)

    # call interface use the default config
    result = tts(token, '语音合成为你的业务增加交互的能力.')
    result_obj = json.loads(result)
    decode_to_wave_file(result_obj['result']['data'], 'data/tts_use_default_config.wav')

    # call interface use the specific config
    result = tts(token, '这里是语音合成的测试。', 'xiaoyu', '0', '16k')
    result_obj = json.loads(result)
    decode_to_wave_file(result_obj['result']['data'], 'data/tts_use_specific_config.wav')