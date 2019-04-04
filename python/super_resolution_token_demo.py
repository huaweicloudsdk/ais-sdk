# -*- coding:utf-8 -*-
from ais_sdk.gettoken import get_token
from ais_sdk.utils import encode_to_base64
from ais_sdk.utils import decode_to_wave_file
from ais_sdk.super_resolution import super_resolution
from ais_sdk.utils import init_global_env
import json

if __name__ == '__main__':
    #
    # access image super resolution enhance,post data by token
    #
    user_name = '*****'
    password = '******'
    account_name = '*****'  # the same as user_name in commonly use
    init_global_env(region='cn-north-1')

    token = get_token(user_name, password, account_name)

    # call interface use base64 file
    result = super_resolution(token, encode_to_base64('data/super-resolution-demo.png'), 3)
    result_obj = json.loads(result)
    decode_to_wave_file(result_obj['result'], 'data/super-resolution-demo-token.png')