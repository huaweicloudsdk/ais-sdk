# -*- coding:utf-8 -*-

import urllib2
import json
import time
import ssl
from gettoken import get_token
from utils import encode_to_base64
from urllib2 import HTTPError, URLError

#
# access asr, long_sentence
#
def long_sentence(token, data, url=''):
    status, r = _long_sentence(token, data, url)

    if status != 200:
        print 'Process long sentence asr failed: summit job failed.'
        return ''

    submit_result = json.loads(r)
    job_id = submit_result['result'].get('job_id', '')
    print "Process job id is :", job_id
    words = ''
    time.sleep(1.0)
    try:
        while True:
            status, r = _get_result(token, job_id)
            if status != 200:
                print 'Process long sentence asr failed: get result failed.'
                break

            rec_result = json.loads(r)

            process_status = rec_result["result"].get('status_code', 1)
            if process_status == -1:
                print 'Process long sentence asr failed: get result failed.'
                break

            elif process_status == 2:
                words = rec_result["result"].get('words', '')
                break

            #
            # process_status == 0 || process_status == 1
            #
            else:
                time.sleep(2.0)
                continue

    except Exception:
        return ''
    return words

#
# long_sentence, post the data
#
def _long_sentence(token, data, url):
    _url = 'https://ais.cn-north-1.myhuaweicloud.com/v1.0/voice/asr/long-sentence'

    _data = {
      "url":url,
      "data": data
    }

    kreq = urllib2.Request( url = _url)
    kreq.add_header('Content-Type', 'application/json')
    kreq.add_header('X-Auth-Token', token )
    kreq.add_data(json.dumps(_data))
    
    resp = None
    status_code = None
    try:
        # 
        # Here we use the unvertified-ssl-context, Because in FunctionStage
        # the client CA-validation have some problem, so we must do this.
        #
        _context = ssl._create_unverified_context()
        r = urllib2.urlopen(kreq, context=_context)
        
    #
    # We use HTTPError and URLError，because urllib2 can't process the 4XX & 
    # 500 error in the single urlopen function.
    #
    # If you use a modern, high-level designed HTTP client lib, Yeah, I mean requests, 
    # there is no this problem. 
    #
    except HTTPError, e:
        resp = e.read()
        status_code = e.code
    except URLError, e:
        resp = e.read()
        status_code = e.code
    else:
        status_code = r.code
        resp = r.read()        
    return status_code, resp

#
# access asr, long_sentence, get the result
#
def _get_result(token, job_id):
    _url_tmpl = 'https://ais.cn-north-1.myhuaweicloud.com/v1.0/voice/asr/long-sentence?job_id=%s'
    _url = _url_tmpl % job_id
    kreq = urllib2.Request( url = _url)
    kreq.add_header('X-Auth-Token', token )
    kreq.add_header('Content-Type', 'application/json')

    resp = None
    status_code = None
    try:
        #
        # Here we use the unvertified-ssl-context, Because in FunctionStage
        # the client CA-validation have some problem, so we must do this.
        #
        _context = ssl._create_unverified_context()
        r = urllib2.urlopen(kreq, context=_context)

        #
        # We use HTTPError and URLError，because urllib2 can't process the 4XX &
        # 500 error in the single urlopen function.
        #
        # If you use a modern, high-level designed HTTP client lib, Yeah, I mean requests,
        # there is no this problem.
        #
    except HTTPError, e:
        resp = e.read()
        status_code = e.code
    except URLError, e:
        resp = e.read()
        status_code = e.code
    else:
        status_code = r.code
        resp = r.read()
    return status_code, resp

if __name__ == '__main__':
    user_name = '******'
    password = '******'
    account_name = '******'  # the same as user_name in commonly use

    demo_data_url = 'https://ais-sample-data.obs.myhwclouds.com/lsr-1.mp3'
    token = get_token(user_name, password, account_name)

    # call interface use the url
    result = long_sentence(token, '', demo_data_url)
    print result

    # call interface use the file
    result = long_sentence(token, encode_to_base64('data/asr-sentence.wav'))
    print result
