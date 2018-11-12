# -*- coding:utf-8 -*-
from ais_sdk.gettoken import get_token
from ais_sdk.utils import encode_to_base64
from ais_sdk.asr_sentence import asr_sentence
from ais_sdk.asr_sentence import asr_sentence_aksk

if __name__ == '__main__':
    #
    # access asr, asr_sentence,post data by token
    #
    user_name = '******'
    password = '******'
    account_name = '******'  # the same as user_name in commonly use

    demo_data_url = 'https://ais-sample-data.obs.myhwclouds.com/asr-sentence.wav'
    token = get_token(user_name, password, account_name)

    # call interface use the url
    result = asr_sentence(token, '', demo_data_url, 'wav', '16k')
    print (result)

    # call interface use the file
    result = asr_sentence(token, encode_to_base64('data/asr-sentence.wav'), '', 'wav', '16k')
    print (result)

    #
    # access asr, asr_sentence,post data by ak,sk
    #
    app_key = "*************"
    app_secret = "************"

    # call interface use the url
    result = asr_sentence_aksk(app_key, app_secret,'', demo_data_url,'wav', '16k')
    print (result)

    # call interface use the file
    result = asr_sentence_aksk(app_key, app_secret, encode_to_base64('data/asr-sentence.wav'), '', 'wav', '16k')
    print (result)