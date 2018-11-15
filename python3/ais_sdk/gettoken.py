import urllib.request
import urllib.parse
import json
import ais_sdk.ais as ais


def get_token(username, password, domain):
    auth_data = {
        "auth": {
            "identity": {

                "password": {
                    "user": {
                        "name": username,
                        "password": password,
                        "domain": {
                            "name": domain
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
    }

    _headers = {
        'Content-Type': 'application/json'
    }
    _url = 'https://' + ais.AisEndpoint.IAM_ENPOINT + ais.TokenURI.AIS_TOKEN

    data = json.dumps(auth_data).encode('utf8')
    req = urllib.request.Request(_url, data, _headers)
    r = urllib.request.urlopen(req)
    X_TOKEN = r.headers['X-Subject-Token']
    return X_TOKEN
