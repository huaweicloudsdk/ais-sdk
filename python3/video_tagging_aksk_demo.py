# -*- coding:utf-8 -*-
from ais_sdk.video_tagging import video_tagging_aksk
from ais_sdk.utils import init_global_env

if __name__ == '__main__':
    # Services currently support North China-Beijing 1 (cn-north-1), Asia Pacific-Hong Kong (ap-southeast-1)
    init_global_env('cn-north-1')

    #
    # access image, video_tagging，post data by ak,sk
    #
    app_key = '*********'
    app_secret = '*********'

    demo_data_url = 'https://obs-test-llg.obs.cn-north-1.myhuaweicloud.com/bgm_recognition'

    # call interface use the url
    result = video_tagging_aksk(app_key, app_secret, demo_data_url, 5, 'zh', 60.0)
    print (result)
