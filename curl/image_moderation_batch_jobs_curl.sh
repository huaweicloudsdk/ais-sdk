#!/bin/sh
>data.json cat <<EOF
{
    "urls":["https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg","https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg"],
    "categories":["terrorism", "porn", "politics"]
}
EOF

#
# Here, if we get the token use the gettoken_curl.sh
#
TOKEN=''
RESULT=$(curl -X POST https://moderation.cn-north-1.myhuaweicloud.com/v1.0/moderation/image/batch/jobs \
  --header 'Content-Type: application/json' \
  --header "X-Auth-Token: $TOKEN" \
  -d "@data.json")
echo RESULT
JOB_ID=`echo $RESULT|awk -F ":" '{print $3}'|awk -F '"' '{print $2}'`


WORDS=''
rm -f headers data.json
while true
do

RESULT_OBJ=$(curl https://moderation.cn-north-1.myhuaweicloud.com/v1.0/moderation/image/batch?job_id=$JOB_ID \
  --header 'Content-Type: application/json' \
  --header "X-Auth-Token: $TOKEN" )
STATUS_CODE=`echo $RESULT_OBJ|awk -F ":" '{print $4}'|awk -F '"' '{print $2}'`

if [ $STATUS_CODE = finish ];
   then
   echo $RESULT_OBJ
   break
elif [ $STATUS_CODE = failed ];
    then
    echo 'the access video service is fialed'
else
    sleep 3
    continue

fi
done
