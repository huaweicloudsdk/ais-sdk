# -*- coding:utf-8 -*-
from ais_sdk.moderation_text import moderation_text_aksk
from ais_sdk.utils import init_global_env

if __name__ == '__main__':
    #
    # access moderation text enhance,posy data by ak,sk
    #
    app_key = '*************'
    app_secret = '************'
    init_global_env(region='cn-north-1')

    # call interface use the text
    result = moderation_text_aksk(app_key, app_secret, '666666luo聊请+110亚砷酸钾六位qq，fuck666666666666666', 'content')
    print result