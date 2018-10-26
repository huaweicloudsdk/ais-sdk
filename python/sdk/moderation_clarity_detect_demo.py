# -*- coding:utf-8 -*-
from ais_sdk.gettoken import get_token
from ais_sdk.utils import encode_to_base64
from ais_sdk.moderation_clarity_detect import moderation_clarity_detect

if __name__ == '__main__':
    user_name = '******'
    password = '******'
    account_name = '******'  # the same as user_name in commonly use

    demo_data_url = 'https://ais-sample-data.obs.myhwclouds.com/tagging-normal.jpg'
    token = get_token(user_name, password, account_name)

    # call interface use the url
    result = moderation_clarity_detect(token, "", demo_data_url, 0.8)
    print result

    # call interface use the file
    result = moderation_clarity_detect(token, encode_to_base64('data/image-tagging-demo-1.jpg'), '', 0.8)
    print result
