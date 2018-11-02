# -*- coding:utf-8 -*-

from ais_sdk.gettoken import get_token
from ais_sdk.utils import encode_to_base64
from ais_sdk.utils import decode_to_wave_file
from ais_sdk.dark_enhance import dark_enhance
from ais_sdk.dark_enhance import dark_enhance_aksk
import json

if __name__ == '__main__':
    #
    # access image dark enhance by token
    #
    user_name = '*****'
    password = '******'
    account_name = '*****'  # the same as user_name in commonly use

    token = get_token(user_name, password, account_name)

    # call interface use base64 file
    result = dark_enhance(token, encode_to_base64('data/dark-enhance-demo-1.bmp'), '0.95')
    print(result)
    result_obj = json.loads(result)
    decode_to_wave_file(result_obj['result'], 'data/dark_enhance_first.bmp')

    #
    # access image dark enhance by ak,sk
    #
    app_key = "*************"
    app_secret = "************"

    # call interface use base64 file
    result = dark_enhance_aksk(app_key,app_secret, encode_to_base64('data/dark-enhance-demo-1.bmp'), '0.95')
    print(result)
    result_obj = json.loads(result)
    decode_to_wave_file(result_obj['result'], 'data/dark_enhance_second.bmp')