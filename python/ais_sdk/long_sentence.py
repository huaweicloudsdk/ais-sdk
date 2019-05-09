# -*- coding:utf-8 -*-

import sys
import time
import json
import ais_sdk.ais as ais
import ais_sdk.utils as utils
import ais_sdk.signer as signer

#
# access asr, long_sentence,post data by token
#
def long_sentence(token, data, url=''):
    status, r = _long_sentence(token, data, url)

    if status != 200:
        return r

    submit_result = json.loads(r)
    job_id = submit_result['result'].get('job_id', '')
    #print "Process job id is :", job_id
    words = ''
    time.sleep(1.0)
    try:
        while True:
            status, r = _get_result(token, job_id)
            if status != 200:
                return r

            rec_result = json.loads(r)

            process_status = rec_result["result"].get('status_code', 1)
            if process_status == -1:
                return r

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
    _url = 'https://%s/v1.0/voice/asr/long-sentence' % ais.AisEndpoint.ASR_ENDPOINT

    if sys.version_info.major >= 3:
        if data != '':
            data = data.decode("utf-8")
    _data = {
        "url": url,
        "data": data
    }

    status, resp = utils.request_token(_url, _data, token)
    if sys.version_info.major < 3:
        return status, resp
    else:
        return status, resp.decode('utf-8')


#
# access asr, long_sentence, get the result
#
def _get_result(token, job_id):
    _url_tmpl = 'https://%s/v1.0/voice/asr/long-sentence?job_id=%s'
    _url = _url_tmpl % (ais.AisEndpoint.ASR_ENDPOINT, job_id)
    return utils.request_job_result_token(_url, token)


#
# access asr, long_sentence,post data by ak,sk
#
def long_sentence_aksk(_ak, _sk, data, url=''):
    sig = signer.Signer()
    sig.AppKey = _ak
    sig.AppSecret = _sk

    status, r = _long_sentence_aksk(sig, data, url)

    if status != 200:
        return r

    submit_result = json.loads(r)
    job_id = submit_result['result'].get('job_id', '')
    #print "Process job id is :", job_id
    words = ''
    time.sleep(1.0)
    try:
        while True:
            status, r = _get_result_aksk(sig, job_id)
            if status != 200:
                return r

            rec_result = json.loads(r)

            process_status = rec_result["result"].get('status_code', 1)
            if process_status == -1:
                return r

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
    _url = 'https://%s/v1.0/voice/asr/long-sentence' % ais.AisEndpoint.ASR_ENDPOINT

    if sys.version_info.major >= 3:
        if data != '':
            data = data.decode("utf-8")

    _data = {
        "url": url,
        "data": data
    }

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = ais.AisEndpoint.ASR_ENDPOINT
    kreq.uri = "/v1.0/voice/asr/long-sentence"
    kreq.method = "POST"
    kreq.headers = {"Content-Type": "application/json"}
    kreq.body = json.dumps(_data)

    status, resp = utils.request_aksk(sig, kreq, _url)
    if sys.version_info.major < 3:
        return status, resp
    else:
        return status, resp.decode('utf-8')


#
# access asr, long_sentence, get the result
#
def _get_result_aksk(sig, job_id):
    _url_tmpl = 'https://%s/v1.0/voice/asr/long-sentence?job_id=%s'
    _url = _url_tmpl % (ais.AisEndpoint.ASR_ENDPOINT, job_id)

    kreq = signer.HttpRequest()
    kreq.scheme = "https"
    kreq.host = ais.AisEndpoint.ASR_ENDPOINT
    kreq.uri = "/v1.0/voice/asr/long-sentence"
    kreq.method = "GET"
    kreq.headers = {"Content-Type": "application/json"}
    kreq.query = {'job_id': job_id}

    return utils.request_job_result_aksk(sig, kreq, _url)
