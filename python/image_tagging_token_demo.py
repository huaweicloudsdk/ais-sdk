# -*- coding:utf-8 -*-
from ais_sdk.gettoken import get_token
from ais_sdk.utils import encode_to_base64
from ais_sdk.image_tagging import image_tagging
from ais_sdk.image_tagging import image_tagging_aksk

if __name__ == '__main__':
    #
    # access image tagging ， post data by token
    #
    user_name = '*******'
    password = '*******'
    account_name = '*******'  # the same as user_name in commonly use
    region_name = '*******'

    token = get_token(user_name, password, account_name, region_name)
    demo_data_url = 'https://obs-ch-sdk-sample.obs.cn-north-1.myhwclouds.com/tagging'


    # call interface use the url
    result = image_tagging(region_name, token, "", demo_data_url, 'zh', 5, 30)
    print result

    # call interface use the file
    result = image_tagging(region_name, token, encode_to_base64('data/image-tagging-demo.jpg'), '', 'zh', 5, 60)
    print result
