# -*- coding:utf-8 -*-
from ais_sdk.utils import encode_to_base64
from ais_sdk.utils import decode_to_wave_file
from ais_sdk.distortion_correct import distortion_correct_aksk
from ais_sdk.utils import init_global_env
import json

if __name__ == '__main__':
    # Services currently support North China-Beijing 1 (cn-north-1), Asia Pacific-Hong Kong (ap-southeast-1)
    init_global_env('cn-north-1')

    #
    # access moderation distortion correct post data by ak,sk
    #
    app_key = '*************'
    app_secret = '************'

    demo_data_url = 'https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/vat-invoice.jpg'

    #call interface use the url correction is true means do not correction
    result = distortion_correct_aksk(app_key, app_secret, "", demo_data_url, True)
    result_obj = json.loads(result)
    if result_obj['result']['data'] != '':
        decode_to_wave_file(result_obj['result']['data'], 'data/modeation-distortion-aksk-1.png')
    else:
        print result

    # call interface use the file
    result = distortion_correct_aksk(app_key, app_secret, encode_to_base64('data/modeation-distortion.jpg'), '', True)
    if result_obj['result']['data'] != '':
        decode_to_wave_file(result_obj['result']['data'], 'data/modeation-distortion-aksk-2.png')
    else:
        print result