#!/bin/sh

USER_NAME=$1
PASSWORD=$2
DOMAIN_NAME=$3
#
# Here, you should substitude the **username** **password** **domainname**
#
curl -X POST https://iam.cn-north-1.myhwclouds.com/v3/auth/tokens \
    --header 'content-type: application/json' \
    -D headers \
    -d '
{
    "auth": {
        "identity": {
           
            "password": {
                "user": {
                    "name": "$USER_NAME", 
                    "password": "$PASSWORD", 
                    "domain": {
                        "name": "$DOMAIN_NAME"
                    }
                }
            },
             "methods": [
                "password"
            ]
        }, 
        "scope": {
            "project": {
                "name": "cn-north-1"
            }
        }
    }
}'

TOKEN=$(grep Token headers | cut -f2 -d: | tr -d ' ')
echo $TOKEN
rm -f headers

