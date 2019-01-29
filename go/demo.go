package main

import (
	"ais_sdk/src/sdk"
	"encoding/json"
	"fmt"
)

func main() {
	ak := "*******" // your AppKey
	sk := "******"  // your AppSecret

	// The sample for asr bgm service
	// Test_BgmAkskDemo(ak, sk)

	// The sample for asr sentence service
	// Test_SentenceAkskDemo(ak, sk)

	// The sample for image clarity detect service
	// Test_ClarityDetectAkskDemo(ak, sk)

	// The sample for image celebrity recognition service
	// Test_CelebrityRecognitionAkskDemo(ak, sk)

	// The sample for image distortion correct service
	// Test_DistortionCorrectAkskDemo(ak, sk)

	// The sample for image anti porn service
	// Test_ImageAntiPornAkskDemo(ak, sk)

	// The sample for image content detect service
	// Test_ImageModerationAkskDemo(ak, sk)

	// The sample for image content batch detect service
	// Test_ImageModerationBatchAkskDemo(ak, sk)

	// The sample for image defog service
	// Test_ImageDefogAkskDemo(ak, sk)

	// The sample for image tagging service
	// Test_ImageTaggingAkskDemo(ak, sk)
	
	// The sample for instrument recognition service
	// Test_InstrumentAkskDemo(ak, sk)

	// The sample for image dark enhance
	// Test_DarkEnhanceAkskDemo(ak, sk)

	// The sample for moderation text
	// Test_ModerationTextAkskDemo(ak, sk)

	// The sample for recapture detect
	// Test_RecaptureDetectAkskDemo(ak, sk)

	// The sample for super resolution
	// Test_SuperResolutionAkskDemo(ak, sk)

	// The sample for text to speech
	// Test_TTSAkskDemo(ak, sk)

	// The sample for long sentence
	// Test_LongSentenceAkskDemo(ak, sk)

	// The sample for long sentence
	   Test_ModerationVideoAkskDemo(ak, sk)
}

func Test_InstrumentAkskDemo(ak string, sk string) {

	// post data by url
	url := "http://img.ikstatic.cn/MTU0NjQ2NzM1MTk0NCM5MzAjanBn.jpg"
	var threshold float32 = 0.5
	result := sdk.InstrumentAksk(ak, sk, "", url, threshold)
	fmt.Println(result)

	// post data by native file
	filepath := "data/instrument.jpg"
	image := sdk.ChangeFileToBase64(filepath)

	result = sdk.InstrumentAksk(ak, sk, image, "", threshold)
	fmt.Println(result)

}

func Test_BgmAkskDemo(ak string, sk string) {

	// post data by url
	url := "https://obs-test-llg.obs.cn-north-1.myhuaweicloud.com/bgm_recognition"
	result := sdk.AsrBgmAksk(ak, sk, url)
	fmt.Println(result)
}

func Test_SentenceAkskDemo(ak string, sk string) {
	encode_type := "wav"
	sample_rate := "16k"

	// post data by url
	url := "https://ais-sample-data.obs.myhuaweicloud.com/asr-sentence.wav"
	result := sdk.AsrSentenceAksk(ak, sk, "", url, encode_type, sample_rate)
	fmt.Println(result)

	// post data by native file
	filepath := "data/asr-sentence.wav"
	data := sdk.ChangeFileToBase64(filepath)
	result = sdk.AsrSentenceAksk(ak, sk, data, "", encode_type, sample_rate)
	fmt.Println(result)
}

func Test_ClarityDetectAkskDemo(ak string, sk string) {
	var threshold float32 = 0.8

	// post data by url
	url := "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/vat-invoice.jpg"
	result := sdk.ClarityDetectAksk(ak, sk, "", url, threshold)
	fmt.Println(result)

	// post data by native file
	filepath := "data/moderation-clarity-detect.jpg"
	image := sdk.ChangeFileToBase64(filepath)
	result = sdk.ClarityDetectAksk(ak, sk, image, "", threshold)
	fmt.Println(result)
}

func Test_CelebrityRecognitionAkskDemo(ak string, sk string) {
	var threshold float32 = 4.8

	// post data by url
	url := "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/celebrity-recognition.jpg"
	result := sdk.CelebrityRecognitionAksk(ak, sk, "", url, threshold)
	fmt.Println(result)

	// post data by native file
	filepath := "data/celebrity-recognition.jpg"
	image := sdk.ChangeFileToBase64(filepath)
	result = sdk.CelebrityRecognitionAksk(ak, sk, image, "", threshold)
	fmt.Println(result)
}

func Test_DarkEnhanceAkskDemo(ak string, sk string) {
	var brightness float32 = 0.9
	var resultMap map[string]interface{}

	filepath := "data/dark-enhance-demo.bmp"
	image := sdk.ChangeFileToBase64(filepath)
	result := sdk.DarkEnhanceAksk(ak, sk, image, brightness)
	json.Unmarshal([]byte(result), &resultMap)
	sdk.Base64ToFile("data/dark-enhance-demo-aksk.bmp", resultMap["result"].(string))
}

func Test_DistortionCorrectAkskDemo(ak string, sk string) {
	var correction bool = true

	// post data by url
	url := "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/vat-invoice.jpg"
	result := sdk.DistortionCorrectAksk(ak, sk, "", url, correction)
	fmt.Println(result)

	// post data by native file
	filepath := "data/modeation-distortion.jpg"
	image := sdk.ChangeFileToBase64(filepath)
	result = sdk.DistortionCorrectAksk(ak, sk, image, "", correction)
	fmt.Println(result)
}

func Test_ImageAntiPornAkskDemo(ak string, sk string) {
	// post data by url
	url := "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg"
	result := sdk.ImageAntiPornAksk(ak, sk, "", url)
	fmt.Println(result)

	// post data by native file
	filepath := "data/moderation-antiporn.jpg"
	image := sdk.ChangeFileToBase64(filepath)
	result = sdk.ImageAntiPornAksk(ak, sk, image, "")
	fmt.Println(result)
}

func Test_ImageModerationAkskDemo(ak string, sk string) {
	var categories = []string{"politics", "terrorism", "porn"}

	// post data by url
	url := "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg"
	result := sdk.ImageModerationAksk(ak, sk, "", url, categories)
	fmt.Println(result)

	// post data by native file
	filepath := "data/moderation-terrorism.jpg"
	image := sdk.ChangeFileToBase64(filepath)
	result = sdk.ImageModerationAksk(ak, sk, image, "", categories)
	fmt.Println(result)
}

func Test_ImageModerationBatchAkskDemo(ak string, sk string) {
	var categories = []string{"politics", "terrorism", "porn"}

	url1 := "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg"
	url2 := "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg"

	var urls = []string{url1, url2}
	result := sdk.ImageModerationBatchAksk(ak, sk, urls, categories)
	fmt.Println(result)
}

func Test_ImageDefogAkskDemo(ak string, sk string) {

	var resultMap map[string]interface{}

	var natural_look bool = true
	var gamma float32 = 1.5

	filepath := "data/defog-demo.png"
	image := sdk.ChangeFileToBase64(filepath)
	result := sdk.ImageDefogAksk(ak, sk, image, gamma, natural_look)
	json.Unmarshal([]byte(result), &resultMap)
	sdk.Base64ToFile("data/defog-demo-aksk.png", resultMap["result"].(string))
}

func Test_ImageTaggingAkskDemo(ak string, sk string) {
	var language string = "en"
	var limit int = -1
	var threshold float32 = 60.0

	// post data by url
	url := "https://ais-sample-data.obs.myhuaweicloud.com/tagging-normal.jpg"
	result := sdk.ImageTaggingAksk(ak, sk, "", url, language, limit, threshold)
	fmt.Println(result)

	// post data by native file
	filepath := "data/image-tagging-demo.jpg"
	image := sdk.ChangeFileToBase64(filepath)
	result = sdk.ImageTaggingAksk(ak, sk, image, "", language, limit, threshold)
	fmt.Println(result)
}

func Test_ModerationTextAkskDemo(ak string, sk string) {

	var categories = []string{"ad", "politics", "flood", "politics", "contraband", "contraband"}

	var text string = "666聊请+110亚砷酸钾六位qq，fuck666666666666666sssssssssss"
	var types string = "content"

	result := sdk.ModerationTextAksk(ak, sk, categories, text, types)
	fmt.Println(result)
}

func Test_RecaptureDetectAkskDemo(ak string, sk string) {

	var scene = []string{"recapture"}
	var threshold float32 = 0.95

	// post data by url
	url := "https://ais-sample-data.obs.myhuaweicloud.com/recapture-detect.jpg"
	result := sdk.RecaptureDetectAksk(ak, sk, "", url, threshold, scene)
	fmt.Println(result)

	// post data by native file
	filepath := "data/recapture-detect-demo.jpg"
	image := sdk.ChangeFileToBase64(filepath)
	result = sdk.RecaptureDetectAksk(ak, sk, image, "", threshold, scene)
	fmt.Println(result)
}

func Test_SuperResolutionAkskDemo(ak string, sk string) {

	var resultMap map[string]interface{}

	var scale int = 3
	var model string = "ESPCN"

	filepath := "data/super-resolution-demo.png"
	image := sdk.ChangeFileToBase64(filepath)
	result := sdk.SuperResolutionAksk(ak, sk, image, scale, model)
	json.Unmarshal([]byte(result), &resultMap)
	sdk.Base64ToFile("data/super-resolution-demo-aksk.png", resultMap["result"].(string))
}

func Test_TTSAkskDemo(ak string, sk string) {

	resultMap := make(map[string]map[string]string)
	resultData := make(map[string]string)

	var text string = "This is a test sample"
	var voiceName string = "xiaoyan"
	var volume int = 0
	var sampleRate string = "16k"
	var speechSpeed int = 0
	var pitchRate int = 0

	result := sdk.TtsAksk(ak, sk, text, voiceName, volume, sampleRate, speechSpeed, pitchRate)
	json.Unmarshal([]byte(result), &resultMap)

	resultData = resultMap["result"]
	sdk.Base64ToFile("data/tts_token_sample.wav", resultData["data"])
}

func Test_LongSentenceAkskDemo(ak string, sk string) {

	// post data by url
	url := "https://ais-sample-data.obs.myhuaweicloud.com/lsr-1.mp3"
	result := sdk.LongSentenceAksk(ak, sk, "", url)
	fmt.Println(result)

	// post data by native file
	filepath := "data/asr-sentence.wav"
	data := sdk.ChangeFileToBase64(filepath)
	result = sdk.LongSentenceAksk(ak, sk, data, "")
	fmt.Println(result)
}

func Test_ModerationVideoAkskDemo(ak string, sk string) {

	// post data by url
	var frameInterval int = 5
	var categories = []string{"politics", "terrorism", "porn"}
	url := "https://obs-test-llg.obs.cn-north-1.myhuaweicloud.com/bgm_recognition"
	result := sdk.ModerationVideoAksk(ak, sk, url, frameInterval, categories)
	fmt.Println(result)
}

func Test_ImageModerationBatchJobAkskDemo(ak string, sk string) {

	// post data by url
	var url1 string = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/terrorism.jpg"
	var url2 string = "https://ais-sample-data.obs.cn-north-1.myhuaweicloud.com/antiporn.jpg"

	var urls = []string{url1, url2}
	var categories = []string{"politics", "terrorism", "porn"}
	result := sdk.ImageModerationBatchJobsAksk(ak, sk, urls, categories)
	fmt.Println(result)
}
