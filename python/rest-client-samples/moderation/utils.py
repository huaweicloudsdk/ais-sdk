# -*- coding:utf-8 -*-
import base64
import urllib
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
