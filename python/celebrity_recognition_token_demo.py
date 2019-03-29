# -*- coding:utf-8 -*-
from ais_sdk.gettoken import get_token
from ais_sdk.utils import encode_to_base64
from ais_sdk.celebrity_recognition import celebrity_recognition

if __name__ == '__main__':
    #
    # access image tagging ， post data by token
    #
    user_name = '******'
    password = '******'
    account_name = '******'  # the same as user_name in commonly use
    region_name = '******'

    demo_data_url = 'https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/celebrity-recognition.jpg'
    token = get_token(user_name, password, account_name,region_name)

    # call interface use the url
    result = celebrity_recognition(region_name, token, "", demo_data_url, 0.48)
    print result

    # call interface use the file
    result = celebrity_recognition(region_name, token, encode_to_base64('data/celebrity-recognition.jpg'), '', 0.48)
    print result