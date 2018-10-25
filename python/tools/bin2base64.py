#!/usr/bin/python
# -*- coding: utf-8 -*-
 
# Tool for convert the file from/to base64-encoding-file.
# usage:
#   python bin2base64.py demo.jpg.b64
#   python bin2base64.py demo.jpg.b64 decode
#   python bin2base64.py demo.jpg encode
#   python bin2base64.py demo.gif
#
# It can be decode and encode the file use the postfix of the file name.
# (It's also named extension of file name or inform the infomation of file type). 
#
# e.g.1 demo.jpg will be encoding, and use the default name demo.jpg.b64, 
# ".b64" means base64-encoded-image file.
#
# e.g.2 demo.jpg.b64 will be decoding, and strip the postfix of file name, 
# then the decoded file name is "demo.jpg".
#
# However, it is not because we known the file type "jpg". Tool just strip the 
# postfix "b64".
#
 
import os, base64
import sys
 
def decode(filename):
    file = open(filename, 'r')
    strs = file.read()
    imgdata = base64.b64decode(strs)
    wf = open("%s" % os.path.splitext(os.path.basename(filename))[0], 'wb')
    wf.write(imgdata)
    wf.close()
    file.close()
 
def encode(filename):
    file = open(filename, 'rb')
    imgstr = base64.b64encode(file.read())
    wf = open("%s.b64" % os.path.basename(filename), 'w+')
    wf.write(imgstr)
    wf.close()
    file.close()
 
def printUsage():
    print '''useage:
        python bin2base64.py demo.jpg.b64
        python bin2base64.py demo.jpg.b64 decode
        python bin2base64.py demo.jpg encode
        python bin2base64.py demo.gif
        '''
 
 
if __name__ == "__main__":
    if len(sys.argv) <2:
        printUsage()
        exit(1)
 
    if len(sys.argv) == 3:
        type = sys.argv[2]
 
    filename = sys.argv[1]
 
    if filename.endswith("b64") or type == "decode":
        decode(filename)
    else:
        encode(filename)

