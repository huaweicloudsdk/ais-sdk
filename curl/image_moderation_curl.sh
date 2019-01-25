#!/bin/sh
>data.json cat <<EOF
{
  "image":"",
  "url":"https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg",
  "categories":["politics", "terrorism", "porn"],
  "threshold":0
}
EOF
#
# Here, if we get the token use the gettoken_curl.sh
#
TOKEN=''
curl -X POST https://moderation.cn-north-1.myhuaweicloud.com/v1.0/moderation/image \
  --header 'Content-Type: application/json' \
  --header "X-Auth-Token: $TOKEN" \
  -d "@data.json"
rm -f headers data.json