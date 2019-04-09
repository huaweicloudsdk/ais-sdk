# -*- coding:utf-8 -*-
from ais_sdk.asr_bgm import asr_bgm_aksk
from ais_sdk.utils import init_global_env

if __name__ == '__main__':
    # Region currently supports Beijing(cn-north-1) and Hong Kong(ap-southeast-1)
    init_global_env('cn-north-1')

    #
    # access asr, asr_bgm,post data by ak,sk
    #
    app_key = '*************'
    app_secret = '************'

    # The OBS link needs to be consistent with the region, and the OBS resources of different regions are not shared
    demo_data_url = 'https://obs-test-llg.obs.cn-north-1.myhuaweicloud.com/bgm_recognition'
    result = asr_bgm_aksk(app_key, app_secret, demo_data_url)
    print result