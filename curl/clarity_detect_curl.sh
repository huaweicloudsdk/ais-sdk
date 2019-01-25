#!/bin/sh

#
# Here, if we get the token use the gettoken_curl.sh
#
TOKEN=''
curl -X POST https://moderation.cn-north-1.myhuaweicloud.com/v1.0/moderation/image/clarity-detect \
  --header 'Content-Type: application/json' \
  --header "X-Auth-Token: $TOKEN" -d'
 {
      "image":"",
      "url":"https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/vat-invoice.jpg",
      "threshold": 0.8,
}'
# if you want to use image paramter, change file to base64,please choose only one paramter in data or url