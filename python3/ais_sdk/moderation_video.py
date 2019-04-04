# -*- coding:utf-8 -*-
import ssl
import ais_sdk.signer as signer
from urllib.error import URLError, HTTPError
import urllib.parse
import urllib.request
import json
import time
import ais_sdk.ais as ais
import ais_sdk.utils as utils

#
# access asr, long_sentence，post data by token
#
def moderation_video(token, url, frame_interval=5, categories=['politics', 'terrorism']):
    endpoint = utils.get_endpoint(ais.AisService.MODERATION_SERVICE)
    status, r = _moderation_video(endpoint, token, url, frame_interval, categories)

    if status != 200:
        return r

    submit_result = json.loads(r)
    job_id = submit_result['result'].get('job_id', '')
    #print("Process job id is :", job_id)
    time.sleep(1.0)
    try:
        while True:
            status, r = _get_result(endpoint, token, job_id)
            if status != 200:
                return r

            rec_result = json.loads(r)

            process_status = rec_result["result"].get('status')
            if process_status == 'failed':
                return r

            elif process_status == 'finish':
                return r

            #
            # process_status == 0 || process_status == 1
            #
            else:
                time.sleep(2.0)
                continue

    except Exception:
        return ''


#
# moderation_video, post the data
#
def _moderation_video(endpoint, token, url, frame_interval=5, categories=['politics', 'terrorism']):
    _url = 'https://%s/v1.0/moderation/video' % endpoint

    _data = {
        "url": url,
        "frame_interval": frame_interval,
        "categories": categories
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
    return status_code, resp.decode('utf-8')


#
# access asr, moderation vedio, get the result
#
def _get_result(endpoint, token, job_id):
    _url_tmpl = 'https://%s/v1.0/moderation/video?job_id=%s'
    _url = _url_tmpl % (endpoint, job_id)
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
# access asr, long_sentence，post data by ak,sk
#
def moderation_video_aksk(_ak, _sk, url, frame_interval=5, categories=['politics', 'terrorism']):
    endpoint = utils.get_endpoint(ais.AisService.MODERATION_SERVICE)
    sig = signer.Signer()
    sig.AppKey = _ak
    sig.AppSecret = _sk

    status, r = _moderation_video_aksk(endpoint, sig, url, frame_interval, categories)

    if status != 200:
        return r

    submit_result = json.loads(r)
    job_id = submit_result['result'].get('job_id', '')
    #print("Process job id is :", job_id)
    time.sleep(1.0)
    try:
        while True:
            status, r = _get_result_aksk(endpoint, sig, job_id)
            if status != 200:
                return r

            rec_result = json.loads(r)

            process_status = rec_result["result"].get('status')
            if process_status == 'failed':
                return r

            elif process_status == 'finish':
                return r

            #
            # process_status == 0 || process_status == 1
            #
            else:
                time.sleep(2.0)
                continue

    except Exception:
        return ''


#
# moderation_video, post the data
#
def _moderation_video_aksk(endpoint, sig, url, frame_interval=5, categories=['politics', 'terrorism']):
    _url = 'https://%s/v1.0/moderation/video' % endpoint

    _data = {
        "url": url,
        "frame_interval": frame_interval,
        "categories": categories
    }

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = endpoint
    kreq.uri = "/v1.0/moderation/video"
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
# access asr, moderation vedio, get the result
#
def _get_result_aksk(endpoint, sig, job_id):
    _url_tmpl = 'https://%s/v1.0/moderation/video?job_id=%s'
    _url = _url_tmpl % (endpoint, job_id)

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = endpoint
    kreq.uri = "/v1.0/moderation/video"
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
