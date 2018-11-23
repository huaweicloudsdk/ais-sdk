# -*- coding:utf-8 -*-
from ais_sdk.utils import encode_to_base64
from ais_sdk.utils import decode_to_wave_file
from ais_sdk.image_defog import image_defog_aksk
import json

if __name__ == '__main__':
    #
    # access image defog,post data by ak,sk
    #
    app_key = '*************'
    app_secret = '************'

    # call interface use base64 file
    result = image_defog_aksk(app_key, app_secret, encode_to_base64('data/defog-demo.png'), '1.5')
    result_obj = json.loads(result)
    decode_to_wave_file(result_obj['result'], 'data/defog-demo-aksk.png')

