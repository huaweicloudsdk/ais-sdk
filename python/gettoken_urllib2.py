import urllib2
import json

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

    _url = 'https://iam.cn-north-1.myhwclouds.com/v3/auth/tokens'

    req = urllib2.Request( url = _url)
    req.add_header('Content-Type', 'application/json')
    req.add_data(json.dumps(auth_data))
    r = urllib2.urlopen(req)
    X_TOKEN = r.headers['X-Subject-Token']
    return X_TOKEN
