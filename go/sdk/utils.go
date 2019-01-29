package sdk

import (
	"encoding/base64"
	"io/ioutil"
	"log"
	"net/http"
	"os"
)

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
