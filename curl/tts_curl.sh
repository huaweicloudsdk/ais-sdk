#!/bin/sh

#
# Here, if we get the token use the gettoken_curl.sh
#
TOKEN=''

curl -X POST https://ais.cn-north-1.myhuaweicloud.com/v1.0/voice/tts \
  --header 'Content-Type: application/json' \
  --header "X-Auth-Token: $TOKEN" -d '
 {
      "text":"This is a test sample",
      "voice_name":"xiaoyan",
      "volume":0,
      "sample_rate":"16k",
      "speech_speed":0,
      "pitch_rate":0
}'
