# -*- coding:utf-8 -*-

from ais_sdk.gettoken import get_token
from ais_sdk.utils import encode_to_base64
from ais_sdk.utils import decode_to_wave_file
from ais_sdk.image_super_resolution import image_super_resolution
import json

if __name__ == '__main__':
    user_name = '*****'
    password = '******'
    account_name = '*****'  # the same as user_name in commonly use

    token = get_token(user_name, password, account_name)

    # call interface use base64 file
    result = image_super_resolution(token, encode_to_base64('data/super-resolution-demo-1.png'), 3)
    print(result)
    result_obj = json.loads(result)
    decode_to_wave_file(result_obj['result'], 'data/super-resolution-demo-first.png')

    # call interface use url