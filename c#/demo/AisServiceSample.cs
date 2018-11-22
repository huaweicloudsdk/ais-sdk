using System;
using Ais.Models;


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


        }

        private static void ImageTagging(String token,String endpoint)
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
    }

}
