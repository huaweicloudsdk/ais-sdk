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

##### access image tagging ####
def image_tagging(token, url):
    _url = 'https://ais.cn-north-1.myhwclouds.com/v1.0/image/tagging'

    _data = {
      "image":"",
      "url":url,
      "language": "zh",
      "limit": 5,
      "threshold": 30.0
    }

    kreq = urllib2.Request( url = _url)
    kreq.add_header('Content-Type', 'application/json')
    kreq.add_header('X-Auth-Token', token )
    kreq.add_data(json.dumps(_data))

    r = urllib2.urlopen(kreq)

    return r.read()

if __name__ == '__main__':
    username = '****'
    password = '****'
    domain = '****'

    _token = get_token(username, password, domain)
    print _token
    file_url = "http://obs-hqq.obs.myhwclouds.com/tagging-normal"
    ret = image_tagging(_token, file_url)
    print ret
