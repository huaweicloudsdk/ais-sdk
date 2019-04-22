# -*- coding:utf-8 -*-
from ais_sdk.gettoken import get_token
from ais_sdk.utils import encode_to_base64
from ais_sdk.utils import decode_to_wave_file
from ais_sdk.distortion_correct import distortion_correct
from ais_sdk.utils import init_global_env
import json

if __name__ == '__main__':
    # Services currently support North China-Beijing 1 (cn-north-1), Asia Pacific-Hong Kong (ap-southeast-1)
    init_global_env('cn-north-1')

    #
    # access moderation distortion correct post data by token
    #
    user_name = '******'
    password = '******'
    account_name = '******'  # the same as user_name in commonly use

    demo_data_url = 'https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/vat-invoice.jpg'
    token = get_token(user_name, password, account_name)

    #call interface use the url correction is true means do not correction
    result = distortion_correct(token, "", demo_data_url, True)
    result_obj = json.loads(result)
    if result_obj['result']['data'] !='':
        decode_to_wave_file(result_obj['result']['data'], 'data/modeation-distortion-token-1.png')
    else:
        print result

    # call interface use the file
    result = distortion_correct(token, encode_to_base64('data/modeation-distortion.jpg'), '', True)
    result_obj = json.loads(result)
    if result_obj['result']['data'] != '':
        decode_to_wave_file(result_obj['result']['data'], 'data/modeation-distortion-token-2.png')
    else:
        print result
