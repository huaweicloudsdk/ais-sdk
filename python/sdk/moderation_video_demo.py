# -*- coding:utf-8 -*-
from ais_sdk.gettoken import get_token
from ais_sdk.utils import encode_to_base64
from ais_sdk.moderation_video import moderation_video

if __name__ == '__main__':
    user_name = '******'
    password = '******'
    account_name = '******'  # the same as user_name in commonly use

    user_name = 'wwfnwg'
    password = 'Huawei@123'
    account_name = 'wwfnwg'  # the same as user_name in commonly use

    demo_data_url = 'https://obs-test-llg.obs.cn-north-1.myhwclouds.com/bgm_recognition'
    token = get_token(user_name, password, account_name)

    # call interface use the url
    result = moderation_video(token, demo_data_url, 5)
    print result