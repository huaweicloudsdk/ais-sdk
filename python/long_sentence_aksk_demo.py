# -*- coding:utf-8 -*-
from ais_sdk.utils import encode_to_base64
from ais_sdk.long_sentence import long_sentence_aksk
from ais_sdk.utils import init_global_env

if __name__ == '__main__':
    # Services currently support North China-Beijing 1 (cn-north-1)
    init_global_env('cn-north-1')

    #
    # access asr, long_sentence,post data by token
    #
    app_key = '*************'
    app_secret = '************'

    demo_data_url = 'https://obs-ch-sdk-sample.obs.cn-north-1.myhwclouds.com/lsr-1.mp3'
    # call interface use the file
    result = long_sentence_aksk(app_key, app_secret, '', demo_data_url)
    print result

    # call interface use the file
    result = long_sentence_aksk(app_key, app_secret, encode_to_base64('data/asr-sentence.wav'))
    print result