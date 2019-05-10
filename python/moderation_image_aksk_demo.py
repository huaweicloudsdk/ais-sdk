# -*- coding:utf-8 -*-
from ais_sdk.utils import encode_to_base64
from ais_sdk.moderation_image import moderation_image_aksk
from ais_sdk.utils import init_global_env

if __name__ == '__main__':
    # Services currently support North China-Beijing 1 (cn-north-1), Asia Pacific-Hong Kong (ap-southeast-1)
    init_global_env('cn-north-1')

    #
    # access moderation image,post data by ak,sk
    #
    app_key = '*************'
    app_secret = '************'

    demo_data_url = 'https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg'

    # call interface use the local file
    result = moderation_image_aksk(app_key, app_secret, encode_to_base64('data/moderation-terrorism.jpg'), "", ['politics','terrorism'], "")
    print(result)

    # call interface use the url
    result = moderation_image_aksk(app_key, app_secret, "", demo_data_url, ['politics','terrorism'], "")
    print(result)


