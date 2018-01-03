#!/bin/sh

#
# Here, if we get the token use the gettoken_curl.sh
#
TOKEN=''

curl -X POST https://ais.cn-north-1.myhwclouds.com/v1.0/image/tagging \
  --header 'Content-Type: application/json' \
  --header "X-Auth-Token: $TOKEN" -d '
 {
      "image":"",
      "url":"http://obs-hqq.obs.myhwclouds.com/tagging-normal",
      "language": "zh",
      "limit": 5,
      "threshold": 30.0
}'
