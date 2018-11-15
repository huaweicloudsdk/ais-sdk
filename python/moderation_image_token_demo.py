# -*- coding:utf-8 -*-
from ais_sdk.gettoken import get_token
from ais_sdk.utils import encode_to_base64
from ais_sdk.moderation_image import moderation_image
if __name__ == '__main__':
    #
    # access moderation image,post data by token
    #
    user_name = '******'
    password = '******'
    account_name = '******'  # the same as user_name in commonly use

    demo_data_url = 'https://ais-sample-data.obs.cn-north-1.myhwclouds.com/terrorism.jpg'
    token = get_token(user_name, password, account_name)

    # call interface use the local file
    result = moderation_image(token, encode_to_base64('data/moderation-terrorism.jpg'), "", ['politics','terrorism'], "")
    print result

    # call interface use the url (token, image, url, threshold=0.95, scene=None)
    result = moderation_image(token, "", demo_data_url, ['politics','terrorism'], "")
    print result