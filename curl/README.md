# AIS服务curl使用示例

## 1.获取Token
### 1.1创建 data.json文件，文件内容如下， 其中，需要修改USERNAME, PASSWORD.
```json
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
curl -X POST https://iam.cn-north-1.myhuaweicloud.com/v3/auth/tokens --header 'content-type: application/json'  -d "@data.json"
```
## 2.申请开通服务
https://support.huaweicloud.com/api-image/image_03_0018.html  图像识别服务申请参考链接
https://support.huaweicloud.com/api-moderation/moderation_03_0013.html  内容审核服务申请参考链接
... ...
## 3.访问图像标签服务
### 3.1 图像标签服务

**图片的base编码方式**
1. 创建data.json文件，内容如下, 其中image的内容为图片的base64编码
```json
{
      "image":"/9j/4AAQSkZJRgABAgEASABIAAD/4RFZRXhpZgAATU0AKgAAAAgABwESAAMAAAABAAEAAAEaAAUAAAABAAAAYgEbAAUAAAABAAAAagEoAAMAAAABAAIAAAExAAIAAAAcAAAAcgEyAAIAAAAUAAAAjodpAAQAAAABAAAApAAAANAACvyAAAAnEAAK/IAAACcQQWRvYmUgUGhvdG9zaG9w......",
      "url":"",
      "language": "zh",
      "limit": 5,
      "threshold": 30.0
}
```
使用如下命令访问
```bash
curl -X POST https://image.cn-north-1.myhuaweicloud.com/v1.0/image/tagging \ --header 'content-type: application/json' \ --header 'x-auth-token: xxxxxxx' -d "@data.json"
```

**OBS URL方式**
1. 创建data.json文件，内容如下, 其中url为华为公有云OBS服务上用户临时读授权的URL, 或者公开读的URL
```json
{
      "image":"",
      "url":"https://ais-sample-data.obs.myhuaweicloud.com/tagging-normal.jpg",
      "language": "zh",
      "limit": 5,
      "threshold": 30.0
}
```
使用如下命令访问
```bash
curl -X POST https://image.cn-north-1.myhuaweicloud.com/v1.0/image/tagging \ --header 'content-type: application/json' \ --header 'x-auth-token: xxxxxxx' -d "@data.json"
```
