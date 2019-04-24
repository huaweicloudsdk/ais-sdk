# -*- coding:utf-8 -*-
from ais_sdk.gettoken import get_token
from ais_sdk.video_tagging import video_tagging
from ais_sdk.utils import init_global_env

if __name__ == '__main__':
    # Services currently support North China-Beijing 1 (cn-north-1), Asia Pacific-Hong Kong (ap-southeast-1)
    init_global_env('cn-north-1')

    #
    # access image, video_tagging，post data by token
    #
    user_name = '*****'
    password = '*****'
    account_name = '*****'  # the same as user_name in commonly use

    demo_data_url = 'https://obs-test-llg.obs.cn-north-1.myhuaweicloud.com/bgm_recognition'

    token = get_token(user_name, password, account_name)

    # call interface use the url
    result = video_tagging(token, demo_data_url, 5, 'zh', 60.0)
    print(result)
