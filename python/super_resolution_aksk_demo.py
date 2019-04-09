# -*- coding:utf-8 -*-
from ais_sdk.utils import encode_to_base64
from ais_sdk.utils import decode_to_wave_file
from ais_sdk.super_resolution import super_resolution_aksk
from ais_sdk.utils import init_global_env
import json

if __name__ == '__main__':
    # Region currently supports Beijing(cn-north-1) and Hong Kong(ap-southeast-1)
    init_global_env('cn-north-1')

    #
    # access image super resolution enhance,post data by sk,sk
    #
    app_key = '*************'
    app_secret = '************'

    # call interface use base64 file
    result = super_resolution_aksk(app_key, app_secret, encode_to_base64('data/super-resolution-demo.png'), 3)
    result_obj = json.loads(result)
    decode_to_wave_file(result_obj['result'], 'data/super-resolution-demo-aksk.png')
