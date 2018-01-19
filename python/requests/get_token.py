#!/usr/bin/python
# -*- coding: utf-8 -*-

import requests
import ais

def get_token(user_name, password, proxy_dict=None):
    """
    process get token for following processing
    :return:
    """
    _urltoken = "%s/v3/auth/tokens" % ais.IAM_ENPOINT
    _headers = {'Content-Type': 'application/json'}
    _paramstoken = {
        "auth": {
            "identity": {
                "methods": ["password"],
                "password": {
                    "user": {
                        "name": user_name,
                        "password": password,
                        "domain": {"name": user_name}}}},
            "scope": {
                "project": {"name": "cn-north-1"}}}
    }
    if proxy_dict is None:
        r = requests.post(_urltoken, json=_paramstoken, headers=_headers)
    else:
        r = requests.post(_urltoken, json=_paramstoken, headers=_headers, proxies=proxy_dict)

    return r.headers.get("X-Subject-Token")


def get_token_with_proxy(user_name, password, proxy_host):
    """
    get token with proxy
    :return:
    """

    _proxy_dict = {
        "https": proxy_host
    }

    return get_token(user_name, password, _proxy_dict)

if __name__ == '__main__':
    #
    # proxy host config
    #
    proxy_user = "******"
    proxy_password = "******"
    https_proxy = "https://%s:%s@proxy.example.com:8080" % (proxy_user, proxy_password)

    #
    # user/password config
    #
    user_name = "******"
    password = "******"
    token = get_token_with_proxy(user_name, password, https_proxy)
