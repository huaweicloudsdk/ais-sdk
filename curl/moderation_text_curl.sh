#!/bin/sh

#
# Here, if we get the token use the gettoken_curl.sh
#
TOKEN=''

curl -X POST https://ais.cn-north-1.myhuaweicloud.com/v1.0/moderation/text \
  --header 'Content-Type: application/json' \
  --header "X-Auth-Token: $TOKEN" -d '
 {
      "categories":"[{"text": "666666luo聊请+110亚砷酸钾六位qq，fuck666666666666666","type": "content"}]",
      "items":"["ad", "politics", "politics", "politics", "contraband", "contraband"]"
}'
