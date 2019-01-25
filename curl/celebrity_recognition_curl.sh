#!/bin/sh

#
# Here, if we get the token use the gettoken_curl.sh
#
TOKEN=''
curl -X POST https://image.cn-north-1.myhuaweicloud.com/v1.0/image/celebrity-recognition \
  --header 'Content-Type: application/json' \
  --header "X-Auth-Token: $TOKEN" -d'
 {
      "image":"",
      "url":"https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/celebrity-recognition.jpg",
      "threshold": 0.48,
}'
# if you want to use image paramter, change file to base64,please choose only one paramter in data or url