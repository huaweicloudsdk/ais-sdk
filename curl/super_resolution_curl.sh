#!/bin/sh
>data.json cat <<EOF
{
  "image":"Qk3WUAoAAAAAADYAAAAoAAAAwgEAAPQBAAABABgAAAAAAKBQCgAAAAAAAAAAAAAAAAAAAAAABAwJBgsJBgsLBgsKBQ...",
  "file":"",
  "gamma": "1.5",
  "natural_look": true
}
EOF
#
# Here, if we get the token use the gettoken_curl.sh
#
TOKEN=''

curl -X POST https://image.cn-north-1.myhuaweicloud.com/v1.0/vision/super-resolution \
  --header 'Content-Type: application/json' \
  --header "X-Auth-Token: $TOKEN" \
  -d "@data.json"
rm -f headers data.json