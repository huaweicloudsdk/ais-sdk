package sdk

import (
	"ais_sdk/src/core"
	"bytes"
	"crypto/tls"
	"encoding/json"
	"io/ioutil"
	"log"
	"net/http"
)

// post data by aksk
func DarkEnhanceAksk(ak string, sk string, image string, brightness float32) string {

	s := core.Signer{
		AppKey:    ak,
		AppSecret: sk,
	}

	requestBody := make(map[string]interface{})
	requestBody["image"] = image
	requestBody["brightness"] = brightness
	bytesData, err := json.Marshal(requestBody)

	if err != nil {
		return err.Error()
	}
	reader := bytes.NewBuffer(bytesData)
	
	endpoint := GetEndpoint(core.IMAGE)
	uri := "https://" + endpoint + core.DARK_ENHANCE
	r, _ := http.NewRequest("POST", uri, reader)

	r.Header.Add("content-type", "application/json")
	s.Sign(r)

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
