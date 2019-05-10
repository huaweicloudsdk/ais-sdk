# -*- coding:utf-8 -*-

import sys
import time
import json
import ais_sdk.ais as ais
import ais_sdk.utils as utils
import ais_sdk.signer as signer

#
# access asr, long_sentence，post data by token
#
_RETRY_TIMES = 3
def moderation_video(token, url, frame_interval=5, categories=['politics','terrorism']):
    endpoint = utils.get_endpoint(ais.AisService.MODERATION_SERVICE)
    status, r = _moderation_video(endpoint, token, url, frame_interval, categories)

    if status != 200:
        return r

    submit_result = json.loads(r)
    job_id = submit_result['result'].get('job_id', '')
    #print "Process job id is :", job_id
    time.sleep(1.0)

    retry_times = 0
    try:
        while True:
            status, r = _get_result(endpoint, token, job_id)
            if status != 200:
                if retry_times < _RETRY_TIMES:
                    retry_times += 1
                    time.sleep(2.0)
                    continue
                else:
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

    status, resp = utils.request_token(_url, _data, token)
    if sys.version_info.major < 3:
        return status, resp
    else:
        return status, resp.decode('utf-8')


#
# access asr, moderation vedio, get the result
#
def _get_result(endpoint, token, job_id):
    _url_tmpl = 'https://%s/v1.0/moderation/video?job_id=%s'
    _url = _url_tmpl % (endpoint, job_id)

    return utils.request_job_result_token(_url, token)


#
# access asr, long_sentence，post data by ak,sk
#
def moderation_video_aksk(_ak, _sk, url, frame_interval=5, categories=['politics', 'terrorism']):
    sig = signer.Signer()
    sig.AppKey = _ak
    sig.AppSecret = _sk

    endpoint = utils.get_endpoint(ais.AisService.MODERATION_SERVICE)

    status, r = _moderation_video_aksk(endpoint, sig, url, frame_interval, categories)

    if status != 200:
        return r

    submit_result = json.loads(r)
    job_id = submit_result['result'].get('job_id', '')
    #print "Process job id is :", job_id
    time.sleep(1.0)

    retry_times = 0
    try:
        while True:
            status, r = _get_result_aksk(endpoint, sig, job_id)
            if status != 200:
                if retry_times < _RETRY_TIMES:
                    retry_times += 1
                    time.sleep(2.0)
                    continue
                else:
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

    status, resp = utils.request_aksk(sig, kreq, _url)
    if sys.version_info.major < 3:
        return status, resp
    else:
        return status, resp.decode('utf-8')


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

    return utils.request_job_result_aksk(sig, kreq, _url)
