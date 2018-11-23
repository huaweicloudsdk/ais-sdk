import urllib2
import json
import ais


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

    _url = 'https://%s/v3/auth/tokens' % ais.AisEndpoint.IAM_ENPOINT

    req = urllib2.Request(url=_url)
    req.add_header('Content-Type', 'application/json')
    req.add_data(json.dumps(auth_data))
    r = urllib2.urlopen(req)
    X_TOKEN = r.headers['X-Subject-Token']
    return X_TOKEN
