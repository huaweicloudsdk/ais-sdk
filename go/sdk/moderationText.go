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

type items struct {
	Type string `json:"type"`
	Text string `json:"text"`
}

type data struct {
	Categories []string `json:"categories"`
	Items      []items  `json:"items"`
}

// post data by aksk
func ModerationTextAksk(ak string, sk string, categories []string, text string, types string) string {

	s := core.Signer{
		AppKey:    ak,
		AppSecret: sk,
	}
	var Items items
	Items.Text = text
	Items.Type = types

	var Data data
	Data.Categories = categories
	Data.Items = []items{Items}

	bytesData, err := json.Marshal(Data)

	if err != nil {
		return err.Error()
	}
	reader := bytes.NewBuffer(bytesData)
	
	endpoint := GetEndpoint(core.MODERATION)
	uri := "https://" + endpoint + core.MODERATION_TEXT
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
