# -*- coding:utf-8 -*-
from ais_sdk.utils import encode_to_base64
from ais_sdk.asr_sentence import asr_sentence_aksk
from ais_sdk.utils import init_global_env

if __name__ == '__main__':
    # Services currently support North China-Beijing 1 (cn-north-1)
    init_global_env('cn-north-1')

    #
    # access asr, asr_sentence,post data by ak,sk
    #
    app_key = '*************'
    app_secret = '************'

    demo_data_url = 'https://obs-ch-sdk-sample.obs.cn-north-1.myhwclouds.com/asr-sentence.wav'

    # call interface use the url
    result = asr_sentence_aksk(app_key, app_secret,'', demo_data_url,'wav', '16k')
    print (result)

    # call interface use the file
    result = asr_sentence_aksk(app_key, app_secret, encode_to_base64('data/asr-sentence.wav'), '', 'wav', '16k')
    print (result)