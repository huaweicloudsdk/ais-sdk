# 如何使用curl来访问华为EI的人工智能相关服务

## 1.获取Token
### 1.1创建 data.json文件，文件内容如下， 其中，需要修改USERNAME, PASSWORD.
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
使用如下命令取得Token, 该Token在Header中：
```bash
curl -X POST https://iam.cn-north-1.myhwclouds.com/v3/auth/tokens --header 'content-type: application/json'  -d "@data.json"
```
## 2.开通人工智能服务
... ...
## 3.访问英文海关单据识别服务
### 3.1 英文海关单据识别服务
**图片的base编码方式**
1. 创建data.json文件，内容如下, 其中image的内容为图片的base64编码
```json
{
"image":"/9j/4AAQSkZJRgABAgEASABIAAD/4RFZRXhpZgAATU0AKgAAAAgABwESAAMAAAABAAEAAAEaAAUAAAABAAAAYgEbAAUAAAABAAAAagEoAAMAAAABAAIAAAExAAIAAAAcAAAAcgEyAAIAAAAUAAAAjodpAAQAAAABAAAApAAAANAACvyAAAAnEAAK/IAAACcQQWRvYmUgUGhvdG9zaG9w......"
}
```
使用如下命令访问
```bash
curl -X POST https://ais.cn-north-1.myhwclouds.com/v1.0/ocr/action/ocr_form --header 'content-type: application/json' --header 'x-auth-token: xxxxxxx' -d "@data.json"
```




