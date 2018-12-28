#!/bin/sh

#
# Here, if we get the token use the gettoken_curl.sh
#
TOKEN=''
curl -X POST https://ais.cn-north-1.myhuaweicloud.com/v1.0/voice/asr/sentence \
  --header 'Content-Type: application/json' \
  --header "X-Auth-Token: $TOKEN" -d'
 {
      "data":"",
      "url":"https://ais-sample-data.obs.myhuaweicloud.com/asr-sentence.wav",
      "encode_type": "wav",
      "sample_rate": "16k"
}'
# if you want to use data paramter, change file to base64,please choose only one paramter in data or url