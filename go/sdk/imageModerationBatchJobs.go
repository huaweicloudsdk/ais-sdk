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

type BatchJobResult struct {
	Status string `json:"status"`
	GobId  string `json:"job_id"`
}

// post data by aksk
func ImageModerationBatchJobsAksk(ak string, sk string, urls []string, categories []string) string {

	s := core.Signer{
		AppKey:    ak,
		AppSecret: sk,
	}

	jobId := GetBatchJobIdAksk(s, urls, categories)

	var resultJson BatchJobResult
	for {
		resultStr := GetBatchJobResult(s, jobId)
		resultMap := make(map[string]BatchJobResult)
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

func GetBatchJobIdAksk(sig core.Signer, urls []string, categories []string) string {
	requestBody := make(map[string]interface{})
	requestBody["urls"] = urls
	requestBody["categories"] = categories
	bytesData, err := json.Marshal(requestBody)

	if err != nil {
		log.Println(err.Error())
	}
	reader := bytes.NewBuffer(bytesData)

	uri := "https://" + core.MODERATION_ENDPOINT + core.IMAGE_MODERATION_BATCH_JOBS
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

func GetBatchJobResult(sig core.Signer, jobId string) string {
	uri := "https://" + core.MODERATION_ENDPOINT + core.IMAGE_MODERATION_BATCH_RESULT + "?job_id=" + jobId
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
