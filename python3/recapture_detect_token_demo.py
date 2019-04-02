# -*- coding:utf-8 -*-
from ais_sdk.gettoken import get_token
from ais_sdk.utils import encode_to_base64
from ais_sdk.recapture_detect import recapture_detect

if __name__ == '__main__':
    #
    # access image recapture detect ,post data by token
    #
    user_name = '******'
    password = '******'
    account_name = '******'  # the same as user_name in commonly use
    region_name = '******'

    demo_data_url = 'https://obs-ch-sdk-sample.obs.cn-north-1.myhwclouds.com/recapture-detect'
    token = get_token(user_name, password, account_name, region_name)

    # call interface use the file
    result = recapture_detect(region_name, token, encode_to_base64('data/recapture-detect-demo.jpg'), "", 0.75, ["recapture"])
    print(result)

    # call interface use the url (token, image, url, threshold=0.95, scene=None)
    result = recapture_detect(region_name, token, "", demo_data_url, 0.75, ["recapture"])
    print (result)
