# -*- coding:utf-8 -*-
import base64
import urllib
import ConfigParser
import os

conf = None

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
    return base64.b64encode(urllib.urlopen(url).read())


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


def get_region_endponit(service_name, region_name):
    global conf
    if conf is None:
        configpath = os.path.join(os.path.dirname(os.path.realpath(__file__)), "endpoints.ini")
        conf = ConfigParser.ConfigParser()
        conf.read(configpath)
        return conf.get(service_name, region_name)
    else:
        return conf.get(service_name, region_name)

