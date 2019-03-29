# -*- coding:utf-8 -*-
from ais_sdk.gettoken import get_token
from ais_sdk.moderation_video import moderation_video

if __name__ == '__main__':
    #
    # access asr, long_sentence，post data by token
    #
    user_name = '******'
    password = '******'
    account_name = '******'  # the same as user_name in commonly use
    region_name = '******'

    demo_data_url = 'https://obs-test-llg.obs.cn-north-1.myhuaweicloud.com/bgm_recognition'
    token = get_token(user_name, password, account_name, region_name)

    # call interface use the url
    result = moderation_video(region_name, token, demo_data_url, 8)
    print result