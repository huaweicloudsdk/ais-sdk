#!/bin/env python

# *coding:utf-8*

def get(self, url, proxy=None):
        if proxy:
            proxy = urllib2.ProxyHandler({'http': proxy})
            opener = urllib2.build_opener(proxy)
            urllib2.install_opener(opener)

        try:
            response = urllib2.urlopen(url)
        except HTTPError, e:
            resp = e.read()
            self.status_code = e.code
        except URLError, e:
            resp = e.read()
            self.status_code = e.code
        else:
            self.status_code = response.code
            resp = response.read()
          
        return resp 
