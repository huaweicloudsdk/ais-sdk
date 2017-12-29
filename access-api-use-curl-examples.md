# 如何使用curl来访问华为EI的人工智能相关服务

##1.获取Token
###1.1创建 data.json文件，文件内容如下， 其中，需要修改USERNAME, PASSWORD.
```javascript
{
    "auth": {
        "identity": {
           
            "password": {
                "user": {
                    "name": "USERNAME", 
                    "password": "PASSWORD", 
                    "domain": {
                        "name": "USERNAME"
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
```
使用如下命令：
```bash
curl -X POST https://iam.cn-north-1.myhwclouds.com/v3/auth/tokens --header 'content-type: application/json'  -d "@data.json"
```
