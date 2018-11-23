#!/bin/sh

#
# Here, if we get the token use the gettoken_curl.sh
#
TOKEN=''
RESULT=$(curl -X POST https://ais.cn-north-1.myhuaweicloud.com/v1.0/voice/asr/long-sentence \
  --header 'Content-Type: application/json' \
  --header "X-Auth-Token: $TOKEN" -d '
 {
      "data":"",
      "url":"https://ais-sample-data.obs.myhwclouds.com/lsr-1.mp3",
      "category":"common"
}')

JOB_ID=`echo $RESULT|awk -F ":" '{print $3}'|awk -F '"' '{print $2}'`

WORDS=''
while true
do
RESULT_OBJ=$(curl https://ais.cn-north-1.myhuaweicloud.com/v1.0/voice/asr/long-sentence?job_id=$JOB_ID \
  --header 'Content-Type: application/json' \
  --header "X-Auth-Token: $TOKEN" )

STATUS_CODE=`echo $RESULT_OBJ|awk -F ":" '{print $3}'|awk -F "," '{print $1}'`

if [ $STATUS_CODE -eq 2 ];
   then
   echo $RESULT_OBJ
   break
elif [ $STATUS_CODE -eq -1 ];
    then
    echo 'the access audio service is fialed'
else
    sleep 3
    continue

fi
done

