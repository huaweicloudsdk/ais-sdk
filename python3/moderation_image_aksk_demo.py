# -*- coding:utf-8 -*-
from ais_sdk.utils import encode_to_base64
from ais_sdk.moderation_image import moderation_image_aksk

if __name__ == '__main__':
    #
    # access moderation image,post data by ak,sk
    #
    app_key = '*************'
    app_secret = '************'
    region_name = '************'

    demo_data_url = 'https://obs-ch-sdk-sample.obs.cn-north-1.myhwclouds.com/terrorism.jpg'

    # call interface use the local file
    result = moderation_image_aksk(region_name, app_key, app_secret, encode_to_base64('data/moderation-terrorism.jpg'),
                                   "", ['politics','terrorism'], "")
    print(result)

    # call interface use the url
    result = moderation_image_aksk(region_name, app_key, app_secret, "", demo_data_url, ['politics','terrorism'], "")
    print(result)


