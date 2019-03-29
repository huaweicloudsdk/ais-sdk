# -*- coding:utf-8 -*-
from ais_sdk.moderation_text import moderation_text_aksk

if __name__ == '__main__':
    #
    # access moderation text enhance,posy data by ak,sk
    #
    app_key = '*************'
    app_secret = '************'
    region_name = '************'

    # call interface use the text
    result = moderation_text_aksk(region_name, app_key, app_secret, '666666luo聊请+110亚砷酸钾六位qq，fuck666666666666666', 'content')
    print(result)