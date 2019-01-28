# -*- coding:utf-8 -*-
from ais_sdk.image_moderation_batch import image_content_batch_aksk

if __name__ == '__main__':
    #
    # access moderation image of batch jobs,post data by ak,sk
    #
    app_key = '*************'
    app_secret = '************'

    demo_data_url1 = 'https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg'
    demo_data_url2 = 'https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg'

    # call interface use the url
    result = image_content_batch_aksk(app_key, app_secret, [demo_data_url1, demo_data_url2], ['politics', 'terrorism'],0)
    print result
