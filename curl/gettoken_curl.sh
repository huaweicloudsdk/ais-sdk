#!/bin/sh

#
# Here, you should substitude the **username** **password** **domainname**
#
curl -X POST https://iam.cn-north-1.myhwclouds.com/v3/auth/tokens --header 'content-type: application/json' -d '
{
    "auth": {
        "identity": {
           
            "password": {
                "user": {
                    "name": "username", 
                    "password": "password", 
                    "domain": {
                        "name": "domainname"
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
