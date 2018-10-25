#!/usr/bin/python
# -*- coding: utf-8 -*-

import base64
import os
import time
import traceback

def encode_to_base64(filename):
    """
    encoding file to base64 encoded stream text
    :param filename:
    :return:
    """
    imgstr = ""
    with open(filename, 'rb') as file:
        imgstr = base64.b64encode(file.read())
    return imgstr


def batch_processing(rootdir, process_func, interval = 1.0):
    """
    process image processing in batch mode
    :param rootdir:
    :return:
    """
    print "Start processing the path: %s ..." % rootdir
    for parent, dirnames, filenames in os.walk(rootdir):
        for filename in filenames:
            # only check the file name with the postfix "jpg, png, tiff"
            if filename.lower().endswith(".jpg") or filename.lower().endswith(".png") or filename == "all":
                filename = "%s/%s" % (parent, filename)

                # if has some error, then log the file name.
                try:
                    data = process_func(filename)
                    time.sleep(interval)
                except Exception as e:
                    print "==================Find Error==================="
                    print "The file processing failed, file name is: %s" % filename
                    print "exception is : %s %s" % (e, traceback.format_exc())
                    print data

    print "End processing the path: %s ..." % rootdir
    

if __name__ == '__main__':
    encode_to_base64(u'pic/乌龟1.jpg')




