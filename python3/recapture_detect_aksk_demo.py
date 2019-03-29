# -*- coding:utf-8 -*-
from ais_sdk.utils import encode_to_base64
from ais_sdk.recapture_detect import recapture_detect_aksk

if __name__ == '__main__':
    #
    # access image recapture detect ,post data by aksk
    #
    app_key = '*************'
    app_secret = '************'
    region_name = '************'

    demo_data_url = 'https://ais-sample-data.obs.myhuaweicloud.com/recapture-detect.jpg'

    # call interface use the file
    result = recapture_detect_aksk(region_name, app_key, app_secret, encode_to_base64('data/recapture-detect-demo.jpg'), "", 0.75, ["recapture"])
    print(result)

    # call interface use the url (token, image, url, threshold=0.95, scene=None)
    result = recapture_detect_aksk(region_name, app_key, app_secret, "", demo_data_url, 0.75, ["recapture"])
    print(result)
