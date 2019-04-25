# -*- coding:utf-8 -*-
from ais_sdk.utils import encode_to_base64, download_url_base64
from ais_sdk.image_antiporn import image_antiporn_aksk
from ais_sdk.utils import init_global_env

if __name__ == '__main__':
    # Services currently support North China-Beijing 1 (cn-north-1), Asia Pacific-Hong Kong (ap-southeast-1)
    init_global_env('cn-north-1')

    #
    # access moderation, image anti-porn,post data by ak,sk
    #
    app_key = '*************'
    app_secret = '************'

    demo_data_url = 'https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg'

    # call interface use the url download
    image_antiporn_aksk(app_key, app_secret, download_url_base64('http://moderation-demo.ei.huaweicloud.com/theme/images/imagga/image_tagging_04.jpg'))

    # call interface use the url
    image_antiporn_aksk(app_key, app_secret, '', demo_data_url )

    # call interface use the file
    image_antiporn_aksk(app_key, app_secret, encode_to_base64('data/moderation-antiporn.jpg'))

