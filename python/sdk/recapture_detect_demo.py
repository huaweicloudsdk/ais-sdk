# -*- coding:utf-8 -*-
from ais_sdk.gettoken import get_token
from ais_sdk.utils import encode_to_base64
from ais_sdk.recapture_detect import recapture_detect
from ais_sdk.recapture_detect import recapture_detect_aksk

if __name__ == '__main__':
    #
    # access image recapture detect ,post data by token
    #
    user_name = '******'
    password = '******'
    account_name = '******'  # the same as user_name in commonly use

    demo_data_url = 'https://ais-sample-data.obs.myhwclouds.com/tagging-normal.jpg'
    token = get_token(user_name, password, account_name)

    # call interface use the url (token, image, url, threshold=0.95, scene=None)
    result = recapture_detect(token, "", demo_data_url, 0.75, ["recapture"])
    print result

    #
    # access image recapture detect ,post data by aksk
    #
    app_key = "*************"
    app_secret = "************"

    # call interface use the file
    result = recapture_detect_aksk(app_key, app_secret, encode_to_base64('data/recapture-detect-demo-1.jpg'), "", 0.75, ["recapture"])
    print result

    demo_data_url = 'https://ais-sample-data.obs.myhwclouds.com/tagging-normal.jpg'

    # call interface use the url (token, image, url, threshold=0.95, scene=None)
    result = recapture_detect_aksk(app_key, app_secret, "", demo_data_url, 0.75, ["recapture"])
    print result
