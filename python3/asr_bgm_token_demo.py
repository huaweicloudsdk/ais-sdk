# -*- coding:utf-8 -*-
from ais_sdk.gettoken import get_token
from ais_sdk.asr_bgm import asr_bgm
from ais_sdk.utils import init_global_env

if __name__ == '__main__':
    #
    # access asr, asr_bgm,post data by token
    #
    user_name = '******'
    password = '******'
    account_name = '******'  # the same as user_name in commonly use
    init_global_env(region='cn-north-1')

    # The OBS link needs to be consistent with the region, and the OBS resources of different regions are not shared
    demo_data_url = 'https://obs-test-llg.obs.cn-north-1.myhuaweicloud.com/bgm_recognition'
    token = get_token(user_name, password, account_name)

    # call interface use the url
    result = asr_bgm(token, demo_data_url)
    print (result)