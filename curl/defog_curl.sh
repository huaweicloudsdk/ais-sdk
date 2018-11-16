#!/bin/sh

#
# Here, if we get the token use the gettoken_curl.sh
#
TOKEN=''
curl -X POST https://ais.cn-north-1.myhuaweicloud.com/v1.0/vision/defog \
  --header 'Content-Type: application/json' \
  --header "X-Auth-Token: $TOKEN" -d '
 {
      "image":"iVBORw0KGgoAAAANSUhEUgAAAbgAAAHCCAIAAAASEUtDAAAAB3RJTUUH3QEMEiEkuvy1dQAAIABJREFUeJw0u0ezZdmRpefuWxx1xbtPy3gvdETqTCCBLACFLuumNtKMM875Jz..", # change file to base64
      "file":"",
      "gamma": "1.5",
      "natural_look": true
}'