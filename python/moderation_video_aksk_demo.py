# -*- coding:utf-8 -*-
from ais_sdk.moderation_video import moderation_video_aksk

if __name__ == '__main__':
    #
    # access asr, long_sentence，post data by ak,sk
    #
    app_key = '*************'
    app_secret = '************'
    region_name = '**********'

    demo_data_url = 'https://obs-test-llg.obs.cn-north-1.myhuaweicloud.com/bgm_recognition'

    # call interface use the url
    result = moderation_video_aksk(region_name, app_key, app_secret, demo_data_url, 8)
    print result
