#!/bin/sh

#
# Here, if we get the token use the gettoken_curl.sh
#
TOKEN=''

curl -X POST https://ais.cn-north-1.myhwclouds.com/v1.0/ocr/action/ocr_form \
  --header 'Content-Type: application/json' \
  --header 'X-Auth-Token: $TOKEN' -d '
{
    "url":""
}
'
