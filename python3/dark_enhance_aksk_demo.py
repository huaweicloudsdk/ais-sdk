# -*- coding:utf-8 -*-
from ais_sdk.utils import encode_to_base64
from ais_sdk.utils import decode_to_wave_file
from ais_sdk.dark_enhance import dark_enhance_aksk
import json

if __name__ == '__main__':
    #
    # access image dark enhance by ak,sk
    #
    app_key = '*************'
    app_secret = '************'

    # call interface use base64 file
    result = dark_enhance_aksk(app_key,app_secret, encode_to_base64('data/dark-enhance-demo.bmp'), '0.95')
    result_obj = json.loads(result)
    decode_to_wave_file(result_obj['result'], 'data/dark-enhance-aksk.bmp')