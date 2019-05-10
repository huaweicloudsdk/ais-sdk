# -*- coding:utf-8 -*-
from ais_sdk.utils import encode_to_base64
from ais_sdk.celebrity_recognition import celebrity_recognition_aksk
from ais_sdk.utils import init_global_env

if __name__ == '__main__':
    # Services currently support North China-Beijing 1 (cn-north-1), Asia Pacific-Hong Kong (ap-southeast-1)
    init_global_env('cn-north-1')

    #
    # access image tagging ， post data by ak,sk
    #
    app_key = '*************'
    app_secret = '************'

    # The OBS link should match the region, and the OBS resources of different regions are not shared
    demo_data_url = 'https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/celebrity-recognition.jpg'

    # call interface use the url
    result = celebrity_recognition_aksk(app_key, app_secret, "", demo_data_url, 0.48)
    print(result)

    # call interface use the file
    result = celebrity_recognition_aksk(app_key, app_secret, encode_to_base64('data/celebrity-recognition.jpg'), '', 0.48)
    print(result)
