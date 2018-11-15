# -*- coding:utf-8 -*-

import urllib2
import json
import time
import ssl
from urllib2 import HTTPError, URLError
import signer
import ais


#
# access asr, long_sentence,post data by token
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
    _url = 'https://%s/v1.0/voice/asr/long-sentence' % ais.AisEndpoint.ENDPOINT

    _data = {
        "url": url,
        "data": data
    }

    kreq = urllib2.Request(url=_url)
    kreq.add_header('Content-Type', 'application/json')
    kreq.add_header('X-Auth-Token', token)
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
    _url_tmpl = 'https://%s/v1.0/voice/asr/long-sentence?job_id=%s'
    _url = _url_tmpl % (ais.AisEndpoint.ENDPOINT, job_id)
    kreq = urllib2.Request(url=_url)
    kreq.add_header('X-Auth-Token', token)
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


#
# access asr, long_sentence,post data by ak,sk
#
def long_sentence_aksk(_ak, _sk, data, url=''):
    sig = signer.Signer()
    sig.AppKey = _ak
    sig.AppSecret = _sk

    status, r = _long_sentence_aksk(sig, data, url)

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
            status, r = _get_result_aksk(sig, job_id)
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
def _long_sentence_aksk(sig, data, url):
    _url = 'https://%s/v1.0/voice/asr/long-sentence' % ais.AisEndpoint.ENDPOINT

    _data = {
        "url": url,
        "data": data
    }

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = ais.AisEndpoint.ENDPOINT
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
        req = urllib2.Request(url=_url, data=kreq.body, headers=kreq.headers)
        r = urllib2.urlopen(req, context=_context)

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
def _get_result_aksk(sig, job_id):
    _url_tmpl = 'https://%s/v1.0/voice/asr/long-sentence?job_id=%s'
    _url = _url_tmpl % (ais.AisEndpoint.ENDPOINT, job_id)

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = ais.AisEndpoint.ENDPOINT
    kreq.uri = "/v1.0/voice/asr/long-sentence"
    kreq.method = "GET"
    kreq.headers = {"Content-Type": "application/json"}
    kreq.query = {'job_id': job_id}
    resp = None
    status_code = None
    try:
        sig.Sign(kreq)
        #
        # Here we use the unvertified-ssl-context, Because in FunctionStage
        # the client CA-validation have some problem, so we must do this.
        #
        _context = ssl._create_unverified_context()
        req = urllib2.Request(url=_url, headers=kreq.headers)
        r = urllib2.urlopen(req, context=_context)

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
