#!/usr/bin/python
# -*- coding: utf-8 -*-
 
import os.path
import sys
 
from yaml import load, Loader
 
def do_validate(rootdir):
    print "Start validate the path: %s ..." % rootdir
    for parent, dirnames, filenames in os.walk(rootdir):
        for filename in filenames:
 
            # only check the file name with the postfix "yaml & yml"
            if filename.endswith(".yaml") or filename.endswith(".yml"):
                filename = "%s/%s" %(parent,filename)
 
                #if has some error, then log the file name.
                try:
                    load(open(filename), Loader=Loader)
                except Exception as e:
                    print "==================Find Error==================="
                    print "The file validate failed, file name is: %s" % filename
                    print "exception is : %s" % e
    print "End validate the path: %s ..." % rootdir
 
if __name__ == "__main__":
    if len(sys.argv) == 2:
        rootdir = sys.argv[1]
        do_validate(rootdir)
    else:
        do_validate(".")
