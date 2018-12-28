# -*- coding:utf-8 -*-
from ais_sdk.asr_bgm import asr_bgm_aksk

if __name__ == '__main__':
    #
    # access asr, asr_bgm,post data by ak,sk
    #
    app_key = '*************'
    app_secret = '************'

    demo_data_url = 'https://obs-test-llg.obs.cn-north-1.myhuaweicloud.com/bgm_recognition'
    result = asr_bgm_aksk(app_key, app_secret, demo_data_url)
    print(result)