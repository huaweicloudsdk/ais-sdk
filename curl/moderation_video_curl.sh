#!/bin/sh

#
# Here, if we get the token use the gettoken_curl.sh
#
TOKEN=''
RESULT=$(curl -X POST https://moderation.cn-north-1.myhuaweicloud.com/v1.0/moderation/video \
  --header 'Content-Type: application/json' \
  --header "X-Auth-Token: $TOKEN" -d '
 {
      "url":"https://obs-test-llg.obs.cn-north-1.myhuaweicloud.com/bgm_recognition",
      "frame_interval":5,
      "category":["terrorism", "porn", "politics"]
}')
JOB_ID=`echo $RESULT|awk -F ":" '{print $3}'|awk -F '"' '{print $2}'`

WORDS=''

while true
do

RESULT_OBJ=$(curl https://moderation.cn-north-1.myhuaweicloud.com/v1.0/moderation/video?job_id=$JOB_ID \
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