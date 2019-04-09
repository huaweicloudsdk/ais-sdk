# -*- coding:utf-8 -*-
from ais_sdk.utils import encode_to_base64
from ais_sdk.recapture_detect import recapture_detect_aksk
from ais_sdk.utils import init_global_env

if __name__ == '__main__':
    # Region currently supports Beijing(cn-north-1) and Hong Kong(ap-southeast-1)
    init_global_env('cn-north-1')

    #
    # access image recapture detect ,post data by aksk
    #
    app_key = '*************'
    app_secret = '************'

    # The OBS link needs to be consistent with the region, and the OBS resources of different regions are not shared
    demo_data_url = 'https://ais-sample-data.obs.myhuaweicloud.com/recapture-detect.jpg'

    # call interface use the file
    result = recapture_detect_aksk(app_key, app_secret, encode_to_base64('data/recapture-detect-demo.jpg'), "", 0.75, ["recapture"])
    print result

    # call interface use the url (token, image, url, threshold=0.95, scene=None)
    result = recapture_detect_aksk(app_key, app_secret, "", demo_data_url, 0.75, ["recapture"])
    print result
