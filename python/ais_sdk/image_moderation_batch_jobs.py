# -*- coding:utf-8 -*-
import sys
import time
import json
import ais_sdk.ais as ais
import ais_sdk.utils as utils
import ais_sdk.signer as signer

#
# access moderation, image content of batch jobs，post data by token
#
_RETRY_TIMES = 3

def image_batch_jobs(token, urls, categories=['politics', 'terrorism', 'porn']):
    endpoint = utils.get_endpoint(ais.AisService.MODERATION_SERVICE)
    status, r = _image_batch_jobs(endpoint, token, urls, categories)

    if status != 200:
        return r

    submit_result = json.loads(r)
    job_id = submit_result['result'].get('job_id', '')
    # print "Process job id is :", job_id
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
# image content batch jobs, post the data
#
def _image_batch_jobs(endpoint, token, urls, categories=['politics', 'terrorism' ,'porn']):
    _url = 'https://%s/v1.0/moderation/image/batch/jobs' % endpoint

    _data = {
        "urls": urls,
        "categories": categories
    }

    status, resp = utils.request_token(_url, _data, token)
    if sys.version_info.major < 3:
        return status, resp
    else:
        return status, resp.decode('utf-8')


#
# access moderation, batch jobs, get the result
#
def _get_result(endpoint, token, job_id):
    _url_tmpl = 'https://%s/v1.0/moderation/image/batch?job_id=%s'
    _url = _url_tmpl % (endpoint, job_id)
    return utils.request_job_result_token(_url, token)



#
# access moderation, image content of batch jobs，post data by ak,sk
#
def image_batch_jobs_aksk(_ak, _sk, urls, categories=['politics', 'terrorism']):
    sig = signer.Signer()
    sig.AppKey = _ak
    sig.AppSecret = _sk

    endpoint = utils.get_endpoint(ais.AisService.MODERATION_SERVICE)

    status, r = _image_batch_jobs_aksk(endpoint, sig, urls, categories)

    if status != 200:
        return r

    submit_result = json.loads(r)
    job_id = submit_result['result'].get('job_id', '')
    # print "Process job id is :", job_id
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
# image content of batch jobs, post the data
#
def _image_batch_jobs_aksk(endpoint, sig, urls, categories=['politics', 'terrorism']):
    _url = 'https://%s/v1.0/moderation/image/batch/jobs' % endpoint

    _data = {
        "urls": urls,
        "categories": categories
    }

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = endpoint
    kreq.uri = "/v1.0/moderation/image/batch/jobs"
    kreq.method = "POST"
    kreq.headers = {"Content-Type": "application/json"}
    kreq.body = json.dumps(_data)

    status, resp = utils.request_aksk(sig, kreq, _url)
    if sys.version_info.major < 3:
        return status, resp
    else:
        return status, resp.decode('utf-8')


#
# access moderation, batch jobs, get the result
#
def _get_result_aksk(endpoint, sig, job_id):
    _url_tmpl = 'https://%s/v1.0/moderation/image/batch?job_id=%s'
    _url = _url_tmpl % (endpoint, job_id)

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = endpoint
    kreq.uri = "/v1.0/moderation/image/batch"
    kreq.method = "GET"
    kreq.headers = {"Content-Type": "application/json"}
    kreq.query = {'job_id': job_id}

    return utils.request_job_result_aksk(sig, kreq, _url)
