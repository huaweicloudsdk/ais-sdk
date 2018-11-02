# -*- coding:utf-8 -*-

import ssl
import signer
from urllib.error import URLError, HTTPError
import urllib.parse
import urllib.request
import json
import time
#
# access asr, long_sentence,post data by token
#
def long_sentence(token, data, url=''):
    status, r = _long_sentence(token, data, url)

    if status != 200:
        print ('Process long sentence asr failed: summit job failed.')
        return ''

    submit_result = json.loads(r)
    job_id = submit_result['result'].get('job_id', '')
    print ("Process job id is :", job_id)
    words = ''
    time.sleep(1.0)
    try:
        while True:
            status, r = _get_result(token, job_id)
            if status != 200:
                print ('Process long sentence asr failed: get result failed.')
                break

            rec_result = json.loads(r)

            process_status = rec_result["result"].get('status_code', 1)
            if process_status == -1:
                print ('Process long sentence asr failed: get result failed.')
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

    except Exception as e:
        print(e)
        return ''
    return words

#
# long_sentence, post the data
#
def _long_sentence(token, data, url):
    _url = 'https://ais.cn-north-1.myhuaweicloud.com/v1.0/voice/asr/long-sentence'

    if data !='':
        data = data.decode("utf-8")

    _data = {
      "url":url,
      "data": data
    }

    _headers = {
        "Content-Type": "application/json",
        "X-Auth-Token": token
    }
    data = json.dumps(_data).encode("utf-8")
    kreq = urllib.request.Request(_url, data, _headers)
    resp = None
    status_code = None
    try:
        #
        # Here we use the unvertified-ssl-context, Because in FunctionStage
        # the client CA-validation have some problem, so we must do this.
        #
        _context = ssl._create_unverified_context()
        r = urllib.request.urlopen(kreq, context=_context)

    #
    # We use HTTPError and URLError，because urllib can't process the 4XX &
    # 500 error in the single urlopen function.
    #
    # If you use a modern, high-level designed HTTP client lib, Yeah, I mean requests,
    # there is no this problem.
    #
    except HTTPError as e:
        resp = e.read()
        status_code = e.code
    except URLError as e:
        resp = e.read()
        status_code = e.code
    else:
        status_code = r.code
        resp = r.read()
    return status_code,resp.decode('utf-8')

#
# access asr, long_sentence, get the result
#
def _get_result(token, job_id):
    _url_tmpl = 'https://ais.cn-north-1.myhuaweicloud.com/v1.0/voice/asr/long-sentence?job_id=%s'
    _url = _url_tmpl % job_id

    _headers = {
        "Content-Type": "application/json",
        "X-Auth-Token": token
    }
    kreq = urllib.request.Request(_url, headers=_headers)

    resp = None
    status_code = None
    try:
        #
        # Here we use the unvertified-ssl-context, Because in FunctionStage
        # the client CA-validation have some problem, so we must do this.
        #
        _context = ssl._create_unverified_context()
        r = urllib.request.urlopen(kreq, context=_context)
    #
    # We use HTTPError and URLError，because urllib can't process the 4XX &
    # 500 error in the single urlopen function.
    #
    # If you use a modern, high-level designed HTTP client lib, Yeah, I mean requests,
    # there is no this problem.
    #
    except HTTPError as e:
        resp = e.read()
        status_code = e.code
    except URLError as e:
        resp = e.read()
        status_code = e.code
    else:
        status_code = r.code
        resp = r.read()
    return status_code, resp.decode('utf-8')


#
# access asr, long_sentence,post data by ak,sk
#
def long_sentence_aksk(_ak,_sk, data, url=''):

    sig = signer.Signer()
    sig.AppKey = _ak
    sig.AppSecret = _sk

    status, r = _long_sentence_aksk(sig, data, url)

    if status != 200:
        print ('Process long sentence asr failed: summit job failed.')
        return ''

    submit_result = json.loads(r)
    job_id = submit_result['result'].get('job_id', '')
    print ("Process job id is :", job_id)
    words = ''
    time.sleep(1.0)
    try:
        while True:
            status, r = _get_result_aksk(sig, job_id)
            if status != 200:
                print ('Process long sentence asr failed: get result failed.')
                break

            rec_result = json.loads(r)

            process_status = rec_result["result"].get('status_code', 1)
            if process_status == -1:
                print ('Process long sentence asr failed: get result failed.')
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
def _long_sentence_aksk(sig, data, url):
    _url = 'https://ais.cn-north-1.myhuaweicloud.com/v1.0/voice/asr/long-sentence'
    if data != '':
         data = data.decode("utf-8")

    _data = {
        "url": url,
        "data": data
    }

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = "ais.cn-north-1.myhuaweicloud.com"
    kreq.uri = "/v1.0/voice/asr/long-sentence"
    kreq.method = "POST"
    kreq.headers = {"Content-Type": "application/json"}
    kreq.body = json.dumps(_data)

    resp = None
    status_code = None
    try:
        sig.Sign(kreq)
        #
        # Here we use the unvertified-ssl-context, Because in FunctionStage
        # the client CA-validation have some problem, so we must do this.
        #
        _context = ssl._create_unverified_context()
        req = urllib.request.Request(url=_url, data=kreq.body, headers=kreq.headers)
        r = urllib.request.urlopen(req, context=_context)

    #
    # We use HTTPError and URLError，because urllib can't process the 4XX &
    # 500 error in the single urlopen function.
    #
    # If you use a modern, high-level designed HTTP client lib, Yeah, I mean requests,
    # there is no this problem.
    #
    except HTTPError as e:
        resp = e.read()
        status_code = e.code
    except URLError as e:
        resp = e.read()
        status_code = e.code
    else:
        status_code = r.code
        resp = r.read()
    return status_code, resp.decode('utf-8')

#
# access asr, long_sentence, get the result
#
def _get_result_aksk(sig, job_id):
    _url_tmpl = 'https://ais.cn-north-1.myhuaweicloud.com/v1.0/voice/asr/long-sentence?job_id=%s'
    _url = _url_tmpl % job_id

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = "ais.cn-north-1.myhuaweicloud.com"
    kreq.uri = "/v1.0/voice/asr/long-sentence"
    kreq.method = "GET"
    kreq.headers = {"Content-Type": "application/json"}
    kreq.query = {'job_id':job_id}
    resp = None
    status_code = None
    try:
        sig.Sign(kreq)
        #
        # Here we use the unvertified-ssl-context, Because in FunctionStage
        # the client CA-validation have some problem, so we must do this.
        #
        _context = ssl._create_unverified_context()
        req = urllib.request.Request(url=_url, headers=kreq.headers)
        r = urllib.request.urlopen(req, context=_context)
    #
    # We use HTTPError and URLError，because urllib can't process the 4XX &
    # 500 error in the single urlopen function.
    #
    # If you use a modern, high-level designed HTTP client lib, Yeah, I mean requests,
    # there is no this problem.
    #
    except HTTPError as e:
        resp = e.read()
        status_code = e.code
    except URLError as e:
        resp = e.read()
        status_code = e.code
    else:
        status_code = r.code
        resp = r.read()
    return status_code, resp.decode('utf-8')
