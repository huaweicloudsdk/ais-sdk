# -*- coding:utf-8 -*-
from ais_sdk.image_content_batch import moderation_image_batch_aksk

if __name__ == '__main__':
    #
    # access moderation image of batch jobs,post data by ak,sk
    #
    app_key = '*************'
    app_secret = '************'

    demo_data_url1 = 'https://ais-sample-data.obs.cn-north-1.myhwclouds.com/terrorism.jpg'
    demo_data_url2 = 'https://ais-sample-data.obs.cn-north-1.myhwclouds.com/antiporn.jpg'

    # call interface use the url
    result = moderation_image_batch_aksk(app_key, app_secret, [demo_data_url1, demo_data_url2],
                                         ['politics', 'terrorism', 'porn'], 0)
    print(result)
