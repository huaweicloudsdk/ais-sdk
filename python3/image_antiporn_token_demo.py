# -*- coding:utf-8 -*-
from ais_sdk.gettoken import get_token
from ais_sdk.utils import encode_to_base64, download_url_base64
from ais_sdk.image_antiporn import image_antiporn
from ais_sdk.utils import init_global_env

if __name__ == '__main__':
    #
    # access moderation, image anti-porn,post data by token
    #
    user_name = '*******'
    password = '*******'
    account_name = '*******'  # the same as user_name in commonly use
    init_global_env(region='cn-north-1')

    demo_data_url = 'https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg'
    token = get_token(user_name, password, account_name)

    # call interface use the url download
    image_antiporn(token, download_url_base64('http://moderation-demo.ei.huaweicloud.com/theme/images/imagga/image_tagging_04.jpg'))

    # call interface use the url

    image_antiporn(token, '', demo_data_url )

    # call interface use the file
    image_antiporn(token, encode_to_base64('data/moderation-antiporn.jpg'))