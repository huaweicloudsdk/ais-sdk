# -*- coding:utf-8 -*-
from ais_sdk.gettoken import get_token
from ais_sdk.image_moderation_batch_jobs import image_batch_jobs

if __name__ == '__main__':
    #
    # access moderation image of batch jobs,post data by token
    #
    user_name = '******'
    password = '******'
    account_name = '******'  # the same as user_name in commonly use

    demo_data_url1 = 'https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg'
    demo_data_url2 = 'https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg'

    token = get_token(user_name, password, account_name)

    # call interface use the url (token, urls, categories )
    result = image_batch_jobs(token, [demo_data_url1,demo_data_url2], ['politics','terrorism'])
    print result