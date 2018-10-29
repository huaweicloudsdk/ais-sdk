# -*- coding:utf-8 -*-
from ais_sdk.gettoken import get_token
from ais_sdk.utils import encode_to_base64
from ais_sdk.utils import decode_to_wave_file
from ais_sdk.distortion_correct import distortion_correct
from ais_sdk.distortion_correct import distortion_correct_aksk
import json

if __name__ == '__main__':
    #
    # access moderation distortion correct.post data by token
    #
    user_name = '******'
    password = '******'
    account_name = '******'  # the same as user_name in commonly use

    demo_data_url = 'https://ais-sample-data.obs.myhwclouds.com/tagging-normal.jpg'
    token = get_token(user_name, password, account_name)

    #call interface use the url correction is true means do not correction
    result = distortion_correct(token, "", demo_data_url, True)
    result_obj = json.loads(result)
    decode_to_wave_file(result_obj['result']['data'], 'data/modeation_distortion-correct_first.png')
    print result

    # call interface use the file
    result = distortion_correct(token, encode_to_base64('data/modeation_distortion.jpg'), '', True)
    result_obj = json.loads(result)
    decode_to_wave_file(result_obj['result']['data'], 'data/modeation_distortion-correct_second.png')
    print result

    #
    # access moderation distortion correct.post data by ak,sk
    app_key = "*************"
    app_secret = "************"

    demo_data_url = 'https://ais-sample-data.obs.myhwclouds.com/tagging-normal.jpg'
    #call interface use the url correction is true means do not correction
    result = distortion_correct_aksk(app_key, app_secret, "", demo_data_url, True)
    result_obj = json.loads(result)
    decode_to_wave_file(result_obj['result']['data'], 'data/modeation_distortion-third.png')
    print result

    # call interface use the file
    result = distortion_correct_aksk(app_key, app_secret, encode_to_base64('data/modeation_distortion.jpg'), '', True)
    print result