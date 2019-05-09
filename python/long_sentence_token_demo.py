# -*- coding:utf-8 -*-
from ais_sdk.gettoken import get_token
from ais_sdk.utils import encode_to_base64
from ais_sdk.long_sentence import long_sentence
from ais_sdk.utils import init_global_env

if __name__ == '__main__':
    # Services currently support North China-Beijing 1 (cn-north-1)
    init_global_env('cn-north-1')

    #
    # access asr, long_sentence,post data by token
    #
    user_name = '******'
    password = '******'
    account_name = '******'  # the same as user_name in commonly use

    demo_data_url = 'https://obs-ch-sdk-sample.obs.cn-north-1.myhwclouds.com/lsr-1.mp3'
    token = get_token(user_name, password, account_name)

    # call interface use the url
    result = long_sentence(token, '', demo_data_url)
    print(result)

    # call interface use the file
    result = long_sentence(token, encode_to_base64('data/asr-sentence.wav'))
    print(result)