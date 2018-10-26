# -*- coding:utf-8 -*-

import urllib2
import json
import time
import ssl
from urllib2 import HTTPError, URLError


#
# access asr, long_sentence
#
def moderation_video(token, url, frame_interval=5, categories=['politics','terrorism']):
    status, r = _moderation_video(token, url,frame_interval,categories)

    if status != 200:
        print 'Process moderation vedio asr failed: summit job failed.'
        return ''

    submit_result = json.loads(r)
    job_id = submit_result['result'].get('job_id', '')
    print "Process job id is :", job_id
    time.sleep(1.0)
    try:
        while True:
            status, r = _get_result(token, job_id)
            if status != 200:
                print 'Process moderation video asr failed: get result failed.'
                break

            rec_result = json.loads(r)

            process_status = rec_result["result"].get('status')
            if process_status == 'failed':
                return r;

            elif process_status == 'finished':
                return r;

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
def _moderation_video(token, url, frame_interval=5, categories=['politics','terrorism']):
    _url = 'https://ais.cn-north-1.myhuaweicloud.com/v1.0/moderation/video'

    _data = {
        "url": url,
        "frame_interval": frame_interval,
        "categories": categories
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
# access asr, moderation vedio, get the result
#
def _get_result(token, job_id):
    _url_tmpl = 'https://ais.cn-north-1.myhuaweicloud.com/v1.0/moderation/video?job_id=%s'
    _url = _url_tmpl % job_id
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


