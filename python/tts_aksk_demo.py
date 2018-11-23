# -*- coding:utf-8 -*-
from ais_sdk.utils import decode_to_wave_file
from ais_sdk.tts import tts_aksk

import json

if __name__ == '__main__':
    #
    # access ocr vat invoice,post data by ak,sk
    #
    app_key = '*************'
    app_secret = '************'

    # call interface use the default config
    result = tts_aksk(app_key, app_secret, '语音合成为你的业务增加交互的能力.')
    result_obj = json.loads(result)
    decode_to_wave_file(result_obj['result']['data'], 'data/tts_use_aksk_default_config.wav')

    # call interface use the specific config
    result = tts_aksk(app_key, app_secret, '这里是语音合成的测试。', 'xiaoyu', '0', '16k')
    result_obj = json.loads(result)
    decode_to_wave_file(result_obj['result']['data'], 'data/tts_use_aksk_specific_config.wav')