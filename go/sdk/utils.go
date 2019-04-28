package sdk

import (
	"encoding/base64"
	"io/ioutil"
	"log"
	"net/http"
	"os"
)

var ENDPOINTS = make(map[string]map[string]string)

func init(){ 
	var imageMap = make(map[string]string)
	var moderationMap = make(map[string]string)
	imageMap["cn-north-1"] = "image.cn-north-1.myhuaweicloud.com"
	imageMap["ap-southeast-1"] = "image.ap-southeast-1.myhuaweicloud.com"
	moderationMap["cn-north-1"] = "moderation.cn-north-1.myhuaweicloud.com"
	moderationMap["ap-southeast-1"] = "moderation.ap-southeast-1.myhuaweicloud.com"
	ENDPOINTS["image"] = imageMap
	ENDPOINTS["moderation"] = moderationMap
}
var region = "cn-north-1"
func ChangeFileToBase64(filepath string) string {
	ff, _ := os.Open(filepath)
	defer ff.Close()

	imgByte, _ := ioutil.ReadAll(ff)
	encodeString := base64.StdEncoding.EncodeToString(imgByte)
	return encodeString
}

func DownFileByUrl(url string) string {

	res, err := http.Get(url)
	if err != nil {
		log.Println(err.Error())
		return ""
	}
	defer res.Body.Close()

	imgByte, _ := ioutil.ReadAll(res.Body)

	encodeString := base64.StdEncoding.EncodeToString(imgByte)
	return encodeString
}

func Base64ToFile(filePath string, base64Str string) {
	buffer, _ := base64.StdEncoding.DecodeString(base64Str)
	err := ioutil.WriteFile(filePath, buffer, 0666)
	if err != nil {
		log.Println(err.Error())
	}
}

func IsOkResponse(statusCode int) bool {
	return statusCode >= 200 && statusCode <= 300
}

func InitRegion(regionName string){
	region = regionName
}

func GetEndpoint(typeName string) string{
	var endpointMap = make(map[string]string)
	endpointMap = ENDPOINTS[typeName]
	return endpointMap[region]
}
