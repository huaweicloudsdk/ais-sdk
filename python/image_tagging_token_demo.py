# -*- coding:utf-8 -*-
from ais_sdk.gettoken import get_token
from ais_sdk.utils import encode_to_base64
from ais_sdk.image_tagging import image_tagging
from ais_sdk.utils import init_global_env

if __name__ == '__main__':
    # Services currently support North China-Beijing 1 (cn-north-1), Asia Pacific-Hong Kong (ap-southeast-1)
    init_global_env('cn-north-1')

    #
    # access image tagging ， post data by token
    #
    user_name = '******'
    password = '******'
    account_name = '******'  # the same as user_name in commonly use

    # The OBS link should match the region, and the OBS resources of different regions are not shared
    demo_data_url = 'https://ais-sample-data.obs.myhuaweicloud.com/tagging-normal.jpg'
    token = get_token(user_name, password, account_name)

    # call interface use the url
    result = image_tagging(token, "", demo_data_url, 'zh', 5, 30)
    print result

    # call interface use the file
    result = image_tagging(token, encode_to_base64('data/image-tagging-demo.jpg'), '', 'zh', 5, 60)
    print result
