# -*- coding:utf-8 -*-
from ais_sdk.gettoken import get_token
from ais_sdk.utils import encode_to_base64, download_url_base64
from ais_sdk.image_antiporn import image_antiporn
from ais_sdk.utils import init_global_env

if __name__ == '__main__':
    # Services currently support North China-Beijing 1 (cn-north-1), Asia Pacific-Hong Kong (ap-southeast-1)
    init_global_env('cn-north-1')

    #
    # access moderation, image anti-porn,post data by token
    #
    user_name = '*******'
    password = '*******'
    account_name = '*******'  # the same as user_name in commonly use

    demo_data_url = 'https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg'
    token = get_token(user_name, password, account_name)

    # call interface use the url download
    image_antiporn(token, download_url_base64('https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg'))

    # call interface use the url
    image_antiporn(token, '', demo_data_url )

    # call interface use the file
    image_antiporn(token, encode_to_base64('data/moderation-antiporn.jpg'))
