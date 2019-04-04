# -*- coding:utf-8 -*-
import os
import base64
import urllib.request
import ais_sdk.ais as ais

_ENDPOINT = {
    'image': {
        'cn-north-1':'image.cn-north-1.myhuaweicloud.com',
        'ap-southeast-1':'image.ap-southeast-1.myhuaweicloud.com'
              },
    'moderation': {
        'cn-north-1':'moderation.cn-north-1.myhuaweicloud.com',
        'ap-southeast-1':'moderation.ap-southeast-1.myhuaweicloud.com'
    }
}


def encode_to_base64(filename):
    """
    encoding file to base64 encoded stream text
    :param filename:
    :return:
    """
    imgstr = ""
    with open(filename, 'rb') as file:
        imgstr = base64.b64encode(file.read())
    return imgstr

def download_url_base64(url):
    return base64.b64encode(urllib.request.urlopen(url).read())

def decode_to_wave_file(base64_encoded_str, filename):
    '''
    decode base64 stream to wave file
    :param base64_encoded_str:
    :return:
    '''
    wave_data = base64.b64decode(base64_encoded_str)
    wf = open(filename, 'wb')
    wf.write(wave_data)
    wf.close()

def get_endpoint(type):
    region_name = get_region()
    return _ENDPOINT[type].get(region_name)

def get_region():
    return os.environ.get(ais.AisService.REGION_MSG)

def init_global_env(region):
    os.environ[ais.AisService.REGION_MSG] = region
