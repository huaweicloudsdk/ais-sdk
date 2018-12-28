# -*- coding:utf-8 -*-
from ais_sdk.utils import encode_to_base64
from ais_sdk.asr_sentence import asr_sentence_aksk

if __name__ == '__main__':
    #
    # access asr, asr_sentence,post data by ak,sk
    #
    app_key = '*************'
    app_secret = '************'

    demo_data_url = 'https://ais-sample-data.obs.myhuaweicloud.com/asr-sentence.wav'

    # call interface use the url
    result = asr_sentence_aksk(app_key, app_secret,'', demo_data_url,'wav', '16k')
    print (result)

    # call interface use the file
    result = asr_sentence_aksk(app_key, app_secret, encode_to_base64('data/asr-sentence.wav'), '', 'wav', '16k')
    print (result)