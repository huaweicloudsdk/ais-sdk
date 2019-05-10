# -*- coding:utf-8 -*-
from ais_sdk.moderation_video import moderation_video_aksk
from ais_sdk.utils import init_global_env

if __name__ == '__main__':
    # Services currently support North China-Beijing 1 (cn-north-1), Asia Pacific-Hong Kong (ap-southeast-1)
    init_global_env('cn-north-1')

    #
    # access asr, long_sentence，post data by ak,sk
    #
    app_key = '*************'
    app_secret = '************'

    demo_data_url = 'https://obs-test-llg.obs.cn-north-1.myhuaweicloud.com/bgm_recognition'

    # call interface use the url
    result = moderation_video_aksk(app_key, app_secret, demo_data_url, 8)
    print(result)
