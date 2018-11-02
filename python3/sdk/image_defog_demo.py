# -*- coding:utf-8 -*-

from ais_sdk.gettoken import get_token
from ais_sdk.utils import encode_to_base64
from ais_sdk.utils import decode_to_wave_file
from ais_sdk.image_defog import image_defog
from ais_sdk.image_defog import image_defog_aksk
import json

if __name__ == '__main__':
    #
    # access image defog,post data by token
    #
    user_name = '*****'
    password = '******'
    account_name = '*****'  # the same as user_name in commonly use

    token = get_token(user_name, password, account_name)

    # call interface use base64 file
    result = image_defog(token, encode_to_base64('data/defog-demo-1.png'), '1.5')
    print(result)
    result_obj = json.loads(result)
    decode_to_wave_file(result_obj['result'], 'data/defog-demo_first.png')

    #
    # access image defog,post data by ak,sk
    #
    app_key = "*************"
    app_secret = "************"

    # call interface use base64 file
    result = image_defog_aksk(app_key, app_secret, encode_to_base64('data/defog-demo-1.png'), '1.5')
    print(result)
    result_obj = json.loads(result)
    decode_to_wave_file(result_obj['result'], 'data/defog-demo_second.png')

