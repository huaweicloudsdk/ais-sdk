#!/bin/sh

#
# Here, if we get the token use the gettoken_curl.sh
#
TOKEN=''

curl -X POST https://moderation.cn-north-1.myhuaweicloud.com/v1.0/moderation/image/anti-porn \
  --header 'Content-Type: application/json' \
  --header "X-Auth-Token: $TOKEN" -d '
 {
      "image":"",
      "url":"https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg"
}'
