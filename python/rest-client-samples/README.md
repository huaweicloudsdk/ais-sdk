
# AIS python Samples

> 说明：
> 当前仅提供使用Token方式访问SDK以及相关的Samples。

+ 每个目录下都有对应的服务的使用的示例代码，请根据文件名查找。例如，短语音识别的内容，查找的目录为：`asr`, 对应的示例代码为 `asr/asr_sentence.py`。
+ common目录下的示例代码，主要给出一些通用的示例, 例如**获取TOKEN**的示例代码：`common/gettoken_urllib2.py`


# python 示例代码的常见问题说明

+ 由于代码中需要进行证书的验证，在某些执行环境，如果无法提供客客户端证书，刚需要如下代码解决。

```python 
    try:
        # 
        # Here we use the unvertified-ssl-context, Because in FunctionStage
        # the client CA-validation have some problem, so we must do this.
        #
        _context = ssl._create_unverified_context()
        r = urllib2.urlopen(kreq, context=_context)
```
+ 使用requests库进行HTTP访问时，需要使用如下代码解决, 设置参数`verify=False`：
```python
r = requests.post(_url, json=_payload, headers=_headers, verify=False)
```




