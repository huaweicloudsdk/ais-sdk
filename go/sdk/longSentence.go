package sdk

import (
	"ais_sdk/src/core"
	"bytes"
	"crypto/tls"
	"encoding/json"
	"io/ioutil"
	"log"
	"net/http"
	"time"
)

type ResultJson struct {
	StatusCode int    `json:"status_code"`
	StatusMsg  string `json:"status_msg"`
	Words      string `json:"words"`
}

// post data by aksk
func LongSentenceAksk(ak string, sk string, data string, url string) string {

	s := core.Signer{
		AppKey:    ak,
		AppSecret: sk,
	}

	jobId := GetLongSentenceJobIdAksk(s, data, url)

	var words string = ""
	var resultJson ResultJson
	for {
		resultStr := GetLongSentenceResult(s, jobId)
		resultMap := make(map[string]ResultJson)
		json.Unmarshal([]byte(resultStr), &resultMap)

		resultJson = resultMap["result"]
		statusCode := resultJson.StatusCode
		if statusCode == -1 {
			return resultStr

		}
		if statusCode == 2 {
			words = resultJson.Words
			break
		} else {
			log.Println("running...")
			time.Sleep(time.Duration(3) * time.Second)
			continue
		}
	}
	return words
}

func GetLongSentenceJobIdAksk(sig core.Signer, data string, url string) string {
	requestBody := make(map[string]interface{})
	requestBody["data"] = data
	requestBody["url"] = url
	bytesData, err := json.Marshal(requestBody)

	if err != nil {
		log.Println(err.Error())
	}
	reader := bytes.NewBuffer(bytesData)

	uri := "https://" + core.ASR_ENDPOINT + core.LONG_SENTENCE
	r, _ := http.NewRequest("POST", uri, reader)

	r.Header.Add("content-type", "application/json")
	sig.Sign(r)

	client := &http.Client{
		Transport: &http.Transport{
			TLSClientConfig: &tls.Config{InsecureSkipVerify: true},
		},
	}

	resp, err := client.Do(r)
	if err != nil {
		log.Println(err.Error())
	}

	defer resp.Body.Close()
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		log.Println(err.Error())
	}
	resultStr := string(body)
	log.Println(resultStr)
	resultMap := make(map[string]map[string]string)
	resultData := make(map[string]string)

	json.Unmarshal([]byte(resultStr), &resultMap)
	resultData = resultMap["result"]

	return resultData["job_id"]
}

func GetLongSentenceResult(sig core.Signer, jobId string) string {
	uri := "https://" + core.ASR_ENDPOINT + core.LONG_SENTENCE + "?job_id=" + jobId
	r, _ := http.NewRequest("GET", uri, nil)

	r.Header.Add("content-type", "application/json")
	sig.Sign(r)

	client := &http.Client{
		Transport: &http.Transport{
			TLSClientConfig: &tls.Config{InsecureSkipVerify: true},
		},
	}

	resp, err := client.Do(r)
	if err != nil {
		log.Println(err.Error())
	}

	defer resp.Body.Close()
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		log.Println(err.Error())
	}
	return string(body)
}
