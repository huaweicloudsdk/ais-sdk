# -*- coding:utf-8 -*-
from ais_sdk.utils import encode_to_base64
from ais_sdk.long_sentence import long_sentence_aksk

if __name__ == '__main__':
    #
    # access asr, long_sentence,post data by token
    #
    app_key = '*************'
    app_secret = '************'

    demo_data_url = 'https://ais-sample-data.obs.myhuaweicloud.com/lsr-1.mp3'
    # call interface use the file
    result = long_sentence_aksk(app_key, app_secret, '', demo_data_url)
    print result

    # call interface use the file
    result = long_sentence_aksk(app_key, app_secret, encode_to_base64('data/asr-sentence.wav'))
    print result