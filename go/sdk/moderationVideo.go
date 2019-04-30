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

type VideoResultJson struct {
	Status string `json:"status"`
	GobId  string `json:"job_id"`
}

// post data by aksk
func ModerationVideoAksk(ak string, sk string, url string, frameInterval int, categories []string) string {

	s := core.Signer{
		AppKey:    ak,
		AppSecret: sk,
	}
	
	endpoint := GetEndpoint(core.MODERATION)
	jobId := GetJobIdAksk(endpoint, s, url, frameInterval, categories)
	var retryTimes int = 0;
	
	var resultJson VideoResultJson
	for {
		resultStr, httpStatusCode := GetResult(endpoint, s, jobId)
		if !IsOkResponse(httpStatusCode) {
			if retryTimes < core.RETRY_MAX_TIMES {
				retryTimes++
				log.Println("Moderation video process is retrying!")
				time.Sleep(time.Duration(3) * time.Second)
				continue
			}else {
				log.Println("Moderation video process is failed")
				return resultStr
			}
		}
		
		resultMap := make(map[string]VideoResultJson)
		json.Unmarshal([]byte(resultStr), &resultMap)

		resultJson = resultMap["result"]
		statusCode := resultJson.Status
		if statusCode == "failed" {
			return resultStr

		}
		if statusCode == "finish" {
			return resultStr
		} else {
			log.Println("running...")
			time.Sleep(time.Duration(3) * time.Second)
			continue
		}
	}
}

func GetJobIdAksk(endpoint string, sig core.Signer, url string, frameInterval int, categories []string) string {
	requestBody := make(map[string]interface{})
	requestBody["url"] = url
	requestBody["frame_interval"] = frameInterval
	requestBody["categories"] = categories
	bytesData, err := json.Marshal(requestBody)

	if err != nil {
		log.Println(err.Error())
	}
	reader := bytes.NewBuffer(bytesData)

	uri := "https://" + endpoint + core.MODERATION_VIDEO
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

func GetResult(endpoint string, sig core.Signer, jobId string) (string, int) {
	uri := "https://" + endpoint + core.MODERATION_VIDEO + "?job_id=" + jobId
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
	statusCode := resp.StatusCode
	defer resp.Body.Close()
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		log.Println(err.Error())
	}
	return string(body), statusCode
}
