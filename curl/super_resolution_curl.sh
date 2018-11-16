#!/bin/sh

#
# Here, if we get the token use the gettoken_curl.sh
#
TOKEN=''

curl -X POST https://ais.cn-north-1.myhuaweicloud.com/v1.0/vision/super-resolution \
  --header 'Content-Type: application/json' \
  --header "X-Auth-Token: $TOKEN" -d '
 {
      "image":"",
      "file":"",
      "scale":3,
      "model":"ESPCN"
}'
# if you want to use image paramter, change file to base64,please choose only one paramter in data or file