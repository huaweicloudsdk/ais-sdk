import sys
import json
import ais_sdk.ais as ais
import ais_sdk.utils as utils

if sys.version_info.major < 3:
    import urllib2

    def get_response(_url, auth_data):
        req = urllib2.Request(url=_url)
        req.add_header('Content-Type', 'application/json')
        req.add_data(json.dumps(auth_data))
        return urllib2.urlopen(req)
else:
    import urllib

    def get_response(_url, auth_data):
        _headers = {
            'Content-Type': 'application/json'
        }

        data = json.dumps(auth_data).encode('utf8')
        req = urllib.request.Request(_url, data, _headers)
        return urllib.request.urlopen(req)


def get_token(username, password, domain):
    region_name = utils.get_region()

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
                    "name": region_name
                }
            }
        }
    }

    _url = 'https://%s/v3/auth/tokens' % ais.AisEndpoint.IAM_ENPOINT

    resp =get_response(_url, auth_data)
    X_TOKEN = resp.headers['X-Subject-Token']
    return X_TOKEN



