using System;
using Ais.Models;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace AisDemo
{
    class AisServiceSample
    {
        static void Main(string[] args)
        {
            String username = "*******";
            String password = "*******";
            String domainName = "*******";
            String regionName = "cn-north-1";

            // service domain name
            String ENDPOINT = "ais.cn-north-1.myhuaweicloud.com";

            // get token domain name 
            String IAM_ENPOINT = "iam.cn-north-1.myhuaweicloud.com";

            String token = Authentication.GetToken(username, domainName, password, regionName, IAM_ENPOINT);

            // image tagging service example
            ImageTagging(token, ENDPOINT);

            // asr bgm service example
            AsrBgm(token, ENDPOINT);

            // asr sentence service example
            AsrSentence(token, ENDPOINT);

            // celebrity recognition service example
            CelebrityRecognition(token, ENDPOINT);

            // clarity detect service example
            ClarityDetect(token, ENDPOINT);

            // dark enhance service example
            DarkEnhance(token, ENDPOINT);

            // distortion correction service example
            DistortionCorrect(token, ENDPOINT);

            // image anti porn service example
            AntiPorn(token, ENDPOINT);

            // image content detect service example
            ImageContent(token, ENDPOINT);

            // image content detect service example
            ImageDefog(token, ENDPOINT);


            // moderation text detect service example
            ModerationText(token, ENDPOINT);

            // image recapture detect service example
            RecaptureDetect(token, ENDPOINT);

            // image super resolution service example
            SuperResolution(token, ENDPOINT);

            // text to speech service example
            Tts(token, ENDPOINT);

            // moderation video service example
            ModerationVideo(token, ENDPOINT);

            // long sentence service example
            LongSentence(token, ENDPOINT);

        }

        private static void ImageTagging(String token, String endpoint)
        {
            String dataUrl = "";    // The obs url of file
            float threshold = 60;   // The confidence interval
            String language = "en"; // The tagging language
            int limit = 5;          // The tagging amount limit of return

            // post data by native file
            String image = utils.ConvertFileToBase64("../../data/image-tagging-demo.jpg");

            String reslut = Image.ImageTaggingToken(token, image, dataUrl, threshold, language, limit, endpoint);
            Console.WriteLine(reslut);

            dataUrl = "https://ais-sample-data.obs.myhwclouds.com/tagging-normal.jpg";

            // post data by obs url
            reslut = Image.ImageTaggingToken(token, "", dataUrl, 60, "en", 5, endpoint);
            Console.WriteLine(reslut);
            Console.ReadKey();

        }

        private static void AsrBgm(String token, String endpoint)
        {
            String dataUrl = "https://obs-test-llg.obs.cn-north-1.myhwclouds.com/bgm_recognition"; // The obs url of file

            // post data by obs url
            String reslut = Asr.AsrBgmToken(token, dataUrl, endpoint);
            Console.WriteLine(reslut);
            Console.ReadKey();
        }

        private static void AsrSentence(String token, String endpoint)
        {
            String encode_type = "wav";     // sentence flie type
            String sample_rate = "16k";     // sampling rate of speech,just adapt to the file suffix is ".wav"
            String dataUrl = "";            // the obs url

            // post data by native file
            String data = utils.ConvertFileToBase64("../../data/asr-sentence.wav");
            String reslut = Asr.AsrSentenceToken(token, data, dataUrl, encode_type, sample_rate, endpoint);
            Console.WriteLine(reslut);

            dataUrl = "https://ais-sample-data.obs.myhwclouds.com/asr-sentence.wav";

            // post data by obs url
            reslut = Asr.AsrSentenceToken(token, "", dataUrl, encode_type, sample_rate, endpoint);
            Console.WriteLine(reslut);
            Console.ReadKey();

        }

        private static void CelebrityRecognition(String token, String endpoint)
        {
            String dataUrl = "";       // The obs url of file
            float threshold = 0.48f;   // The confidence interval,default 0.48f (0-1)

            // post data by native file
            String data = utils.ConvertFileToBase64("../../data/celebrity-recognition.jpg");
            String reslut = Image.CelebrityRecognitionToken(token, data, dataUrl, threshold, endpoint);
            Console.WriteLine(reslut);

            dataUrl = "https://ais-sample-data.obs.cn-north-1.myhwclouds.com/celebrity-recognition.jpg";

            // post data by obs url
            reslut = Image.CelebrityRecognitionToken(token, "", dataUrl, threshold, endpoint);
            Console.WriteLine(reslut);
            Console.ReadKey();

        }

        private static void ClarityDetect(String token, String endpoint)
        {
            String dataUrl = "";      // The obs url of file
            float threshold = 0.8f;   // The clarity confidence interval,default 0.8f

            // post data by native file
            String data = utils.ConvertFileToBase64("../../data/moderation-clarity-detect.jpg");
            String reslut = Moderation.ClarityDetectToken(token, data, dataUrl, threshold, endpoint);
            Console.WriteLine(reslut);

            dataUrl = "https://ais-sample-data.obs.cn-north-1.myhwclouds.com/vat-invoice.jpg";

            // post data by obs url
            reslut = Moderation.ClarityDetectToken(token, "", dataUrl, threshold, endpoint);
            Console.WriteLine(reslut);
            Console.ReadKey();

        }

        private static void DarkEnhance(String token, String endpoint)
        {
            float brightness = 0.9f;   // The brightness interval,default 0.9f

            // post data by native file
            String data = utils.ConvertFileToBase64("../../data/dark-enhance-demo.bmp");

            String reslut = Image.DarkEnhanceToken(token, data, brightness, endpoint);
            JObject joResult = (JObject)JsonConvert.DeserializeObject(reslut);
            String filePath = utils.Base64ToFileAndSave(joResult["result"].ToString(), @"../../data/dark-enhance-token.bmp");
            Console.WriteLine(filePath);
            Console.ReadKey();
        }

        private static void DistortionCorrect(String token, String endpoint)
        {
            String dataUrl = "";       // The obs url of file
            bool correction = false;   // Whether to correct distortion or not

            // post data by native file
            String data = utils.ConvertFileToBase64("../../data/modeation-distortion.jpg");
            String reslut = Moderation.DistortionCorrectToken(token, data, dataUrl, correction, endpoint);
            JObject joResult = (JObject)JsonConvert.DeserializeObject(reslut);
            if (joResult["result"]["data"].ToString() != "")
            {
                String resultPath = @"../../data/modeation-distortion-token-1.bmp";
                resultPath = utils.Base64ToFileAndSave(joResult["result"]["data"].ToString(), resultPath);
                Console.WriteLine(resultPath);
            }
            else
            {
                Console.WriteLine(reslut);
            }


            dataUrl = "https://ais-sample-data.obs.cn-north-1.myhwclouds.com/vat-invoice.jpg";

            // post data by obs url
            reslut = Moderation.DistortionCorrectToken(token, "", dataUrl, correction, endpoint);
            joResult = (JObject)JsonConvert.DeserializeObject(reslut);
            if (joResult["result"]["data"].ToString() != "")
            {
                String resultPath = @"../../data/modeation-distortion-token-2.bmp";
                resultPath = utils.Base64ToFileAndSave(joResult["result"]["data"].ToString(), resultPath);
                Console.WriteLine(resultPath);
            }
            else
            {
                Console.WriteLine(reslut);
            }
            Console.ReadKey();

        }

        private static void AntiPorn(String token, String endpoint)
        {

            String dataUrl = "";       // The obs url of file

            // post data by native file
            String data = utils.ConvertFileToBase64("../../data/moderation-antiporn.jpg");
            String reslut = Moderation.AntiPornToken(token, data, dataUrl, endpoint);
            Console.WriteLine(reslut);

            dataUrl = "https://ais-sample-data.obs.cn-north-1.myhwclouds.com/antiporn.jpg";

            // post data by obs url
            reslut = Moderation.AntiPornToken(token, "", dataUrl, endpoint);
            Console.WriteLine(reslut);
            Console.ReadKey();
        }

        private static void ImageContent(String token, String endpoint)
        {

            String dataUrl = "";                                                        // The obs url of file
            float threshold = 0.6f;                                                     // The image content confidence interval,"politics" default 0.48f,"terrorism":0

            JArray categories = new JArray();                                           // The scene of detect 

            categories.Add("politics");
            categories.Add("terrorism");
            categories.Add("porn");

            // post data by native file
            String data = utils.ConvertFileToBase64("../../data/moderation-terrorism.jpg");
            String reslut = Moderation.ImageContentToken(token, data, dataUrl, threshold, categories, endpoint);
            Console.WriteLine(reslut);

            dataUrl = "https://ais-sample-data.obs.cn-north-1.myhwclouds.com/terrorism.jpg";

            // post data by obs url
            reslut = Moderation.ImageContentToken(token, "", dataUrl, threshold, categories, endpoint);
            Console.WriteLine(reslut);
            Console.ReadKey();
        }

        private static void ImageDefog(String token, String endpoint)
        {

            bool natural_look = true;       // Is natural 
            float gamma = 1.5f;             // The gama correction value,default 1.5. range : [0.1,10]

            // post data by native file
            String data = utils.ConvertFileToBase64("../../data/defog-demo.png");
            String reslut = Image.ImageDefogToken(token, data, gamma, natural_look, endpoint);

            JObject joResult = (JObject)JsonConvert.DeserializeObject(reslut);
            String resultPath = utils.Base64ToFileAndSave(joResult["result"].ToString(), @"../../data/defog-demo-token.png");

            Console.WriteLine(resultPath);
            Console.ReadKey();
        }

        private static void ModerationText(String token, String endpoint)
        {
            JArray categories = new JArray();                                           // The scene of detect 
            categories.Add("politics");
            categories.Add("porn");
            categories.Add("contraband");
            categories.Add("ad");

            JArray items = new JArray();                                             // The content of detect
            JObject content = new JObject();
            content.Add("text", "666666luo聊请+110亚砷酸钾六位qq，fuck666666666666666");
            content.Add("type", "content");
            items.Add(content);

            String reslut = Moderation.ModerationTextToken(token, categories, items, endpoint);
            Console.WriteLine(reslut);
            Console.ReadKey();
        }

        private static void RecaptureDetect(String token, String endpoint)
        {
            String dataUrl = "";            // The obs url of file
            float threshold = 0.95f;        // The image content confidence interval,"politics" default 0.95f.range of  [0-1]    
            JArray scene = new JArray();    // The scene of recapture detect
            scene.Add("recapture");

            // post data by native file
            String data = utils.ConvertFileToBase64("../../data/recapture-detect-demo.jpg");
            String reslut = Image.RecaptureDetectToken(token, data, dataUrl, threshold, scene, endpoint);
            Console.WriteLine(reslut);

            dataUrl = "https://ais-sample-data.obs.myhwclouds.com/recapture-detect.jpg";

            // post data by obs url
            reslut = Image.RecaptureDetectToken(token, "", dataUrl, threshold, scene, endpoint);
            Console.WriteLine(reslut);
            Console.ReadKey();
        }

        private static void SuperResolution(String token, String endpoint)
        {
            int scale = 3;                  // The result of magnification， default 3 ,only 3 or 4   
            String model = "ESPCN";         // The algorithm pattern,default ESPCN

            // post data by native file
            String data = utils.ConvertFileToBase64("../../data/super-resolution-demo.png");
            String reslut = Image.SuperResolutionToken(token, data, scale, model, endpoint);

            JObject joResult = (JObject)JsonConvert.DeserializeObject(reslut);
            String filePath = utils.Base64ToFileAndSave(joResult["result"].ToString(), @"../../data/super-resolution-token.png");
            Console.WriteLine(filePath);
            Console.ReadKey();
        }

        private static void Tts(String token, String endpoint)
        {
            String text = "This is a test sample";   // Text in speech synthesis   
            String voice_name = "xiaoyan";           // The voice name for voice output
            int volume = 0;                          // The volume for voice output. [-20, 20]，default value is 0。
            String sample_rate = "16k";              // The sample rate
            int speech_speed = 0;                    // [-500, 500]
            int pitch_rate = 0;                      // [-500, 500]

            // post data by native file

            String reslut = TTS.TTSToken(token, text, voice_name, volume, sample_rate, speech_speed, pitch_rate, endpoint);

            JObject joResult = (JObject)JsonConvert.DeserializeObject(reslut);
            String filePath = utils.Base64ToFileAndSave(joResult["result"]["data"].ToString(), @"../../data/tts_token_sample.wav");
            Console.WriteLine(filePath);
            Console.ReadKey();
        }

        private static void ModerationVideo(String token, String endpoint)
        {

            JArray categories = new JArray();                                                    // The scene of detect 
            categories.Add("terrorism");
            categories.Add("porn");
            categories.Add("politics");

            String url = "https://obs-test-llg.obs.cn-north-1.myhwclouds.com/bgm_recognition";   // OBS URL of the video  
            int frame_interval = 5;                                                              // Frame time interval

            String reslut = Moderation.VideoToken(token, url, frame_interval, categories, endpoint);
            Console.WriteLine(reslut);
            Console.ReadKey();
        }

        private static void LongSentence(String token, String endpoint)
        {

            String dataUrl = "";                                                        // The obs url of file
            String categories = "common";                                               // The scene of detect 

            // post data by native file
            String data = utils.ConvertFileToBase64("../../data/asr-sentence.wav");
            String reslut = Asr.LongSentenceToken(token, data, dataUrl, categories, endpoint);
            Console.WriteLine(reslut);

            dataUrl = "https://ais-sample-data.obs.myhwclouds.com/lsr-1.mp3";

            // post data by obs url
            reslut = Asr.LongSentenceToken(token, "", dataUrl, categories, endpoint);
            Console.WriteLine(reslut);
            Console.ReadKey();
        }
    }
}
