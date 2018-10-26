# -*- coding:utf-8 -*-
from ais_sdk.gettoken import get_token
from ais_sdk.utils import encode_to_base64
from ais_sdk.image_recapture_detect import image_recapture_detect

if __name__ == '__main__':
    user_name = '******'
    password = '******'
    account_name = '******'  # the same as user_name in commonly use

    demo_data_url = 'https://ais-sample-data.obs.myhwclouds.com/tagging-normal.jpg'
    token = get_token(user_name, password, account_name)

    # call interface use the url (token, image, url, threshold=0.95, scene=None)
    result = image_recapture_detect(token, "", demo_data_url, 0.75, ["recapture"])
    print result

    # call interface use the file
    result = image_recapture_detect(token, encode_to_base64('data/recapture-detect-demo-1.jpg'), "", 0.75, ["recapture"])
    print result
