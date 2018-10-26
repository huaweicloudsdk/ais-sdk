# -*- coding:utf-8 -*-
from ais_sdk.gettoken import get_token
from ais_sdk.utils import encode_to_base64
from ais_sdk.utils import decode_to_wave_file
from ais_sdk.moderation_distortion_correct import moderation_distortion_correct
import json

if __name__ == '__main__':
    user_name = '******'
    password = '******'
    account_name = '******'  # the same as user_name in commonly use

    demo_data_url = 'https://ais-sample-data.obs.myhwclouds.com/tagging-normal.jpg'
    token = get_token(user_name, password, account_name)

    #call interface use the url correction is true means do not correction
    result = moderation_distortion_correct(token, "", demo_data_url, True)
    result_obj = json.loads(result)
    decode_to_wave_file(result_obj['result']['data'], 'data/modeation_distortion-correct.png')
    print result

    # call interface use the file
    result = moderation_distortion_correct(token, encode_to_base64('data/modeation_distortion.jpg'), '', True)
    print result
