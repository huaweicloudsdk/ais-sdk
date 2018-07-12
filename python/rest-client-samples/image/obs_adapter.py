#!/bin/env python
# -*- coding:utf-8 -*-
#download obs lib from https://bbs.huaweicloud.com/forum/thread-9056-1-1.html, ,run 'python setup.py install ' to install obs client lib
from com.obs.client.obs_client import ObsClient 
import uuid
import sys
import os
import time
import urllib2
from urllib2 import HTTPError, URLError

def upload_file_2_obs(client, bucket, prefix, local_file, expires = 3600):

    object_key = "%s_%s_%s" %(prefix, str(uuid.uuid1()), open(local_file, 'r').name)

    #upload file
    resp = client.putFile(bucket, object_key, file_path=local_file)
    
    if resp.status >= 300:
        return False, resp.errorMessage, ''

    method='GET'
    res = client.createV2SignedUrl(method, bucket, object_key)
    return True, object_key, res['signedUrl']


def download_url_file(url, file_name):
    try:
        r = urllib2.urlopen(url)
    except HTTPError, e:
        resp = e.read()
        rc = e.code
    except URLError, e:
        resp = e.read()
        rc = e.code
    else:
        resp = r.read()
        rc = r.code

    if rc != 200:
        return False
    f = open(file_name, 'wb')
    f.write(resp)
    f.close()
    return True

#1. download web url to import obs
#2. get obs read temp url
def change_weburl_2_obs(url, client, bucket, prefix, temp_file):
    if not download_url_file(url, temp_file):
        return False, ''
    flag, key, url = upload_file_2_obs(client, bucket, prefix, temp_file)
    os.remove(temp_file)
    return flag, url

if __name__ == "__main__":
    client = ObsClient( 
        access_key_id='OBS AK',
        secret_access_key='OBS SK',
        server='obs.cn-north-1.myhwclouds.com' 
    ) 
    bucket = 'bucketXXX'
   
    print change_weburl_2_obs('http://c.hiphotos.baidu.com/image/pic/item/ae51f3deb48f8c540e2d1dd336292df5e1fe7f54.jpg', client, bucket, 'WEBURL', 'a.jpg')

    client.close()

    #print upload_file_2_obs(client, bucket, 'MANUAL', 'a.jpg')
    #print download_url_file('http://c.hiphotos.baidu.com/image/pic/item/ae51f3deb48f8c540e2d1dd336292df5e1fe7f54.jpg', 'a.jpg')
