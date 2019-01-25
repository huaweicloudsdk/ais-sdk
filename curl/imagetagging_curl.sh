#!/bin/sh

#
# Here, if we get the token use the gettoken_curl.sh
#
TOKEN=''

curl -X POST https://image.cn-north-1.myhuaweicloud.com/v1.0/image/tagging \
  --header 'Content-Type: application/json' \
  --header "X-Auth-Token: $TOKEN" -d '
 {
      "image":"",
      "url":"https://ais-sample-data.obs.myhuaweicloud.com/tagging-normal.jpg",
      "language": "zh",
      "limit": 5,
      "threshold": 30.0
}'
