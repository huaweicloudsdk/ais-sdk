# -*- coding:utf-8 -*-

from ais_sdk.gettoken import get_token
from ais_sdk.utils import encode_to_base64
from ais_sdk.utils import decode_to_wave_file
from ais_sdk.image_dark_enhance import image_dark_enhance
import json

if __name__ == '__main__':
    user_name = '*****'
    password = '******'
    account_name = '*****'  # the same as user_name in commonly use

    token = get_token(user_name, password, account_name)

    # call interface use base64 file
    result = image_dark_enhance(token, encode_to_base64('data/dark-enhance-demo-1.bmp'), '0.95')
    print(result)
    result_obj = json.loads(result)
    decode_to_wave_file(result_obj['result'], 'data/dark_enhance_first.bmp')