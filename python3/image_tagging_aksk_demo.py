# -*- coding:utf-8 -*-
from ais_sdk.utils import encode_to_base64
from ais_sdk.image_tagging import image_tagging_aksk

if __name__ == '__main__':
    #
    # access image tagging ， post data by ak,sk
    #
    app_key = '*************'
    app_secret = '************'
    region_name = '************'

    demo_data_url = 'https://obs-ch-sdk-sample.obs.cn-north-1.myhwclouds.com/tagging'

    # call interface use the url
    result = image_tagging_aksk(region_name, app_key, app_secret, "", demo_data_url, 'zh', 5, 30)
    print(result)

    # call interface use the file
    result = image_tagging_aksk(region_name, app_key, app_secret, encode_to_base64('data/image-tagging-demo.jpg'), '', 'zh', 5, 60)
    print(result)
