using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Net.Security;
using System.Security.Cryptography.X509Certificates;
using System.Text;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace Ais.Models
{
    public class Image
    {
        private static Dictionary<String, String> endPointsDic = new Dictionary<string, string>();

        static Image()
        {
            endPointsDic.Add("cn-north-1", "image.cn-north-1.myhuaweicloud.com");
            endPointsDic.Add("ap-southeast-1", "image.ap-southeast-1.myhuaweicloud.com");
        }
        public static String ImageTaggingToken(String token, String image, String url, float threshold, String language, int limit, String endpoint)
        {
            // reuqest data for image tagging
            JObject requestBody = new JObject();
            requestBody.Add("image", image);
            requestBody.Add("url", url);
            requestBody.Add("threshold", threshold);
            requestBody.Add("limit", limit);
            requestBody.Add("language", language);

            HttpWebRequest request = null;
            String result = null;
            String uri = new StringBuilder().Append("https://").Append(endpoint).Append(Ais.IMAGE_TAGGING).ToString();

            String serviceName = System.Reflection.MethodBase.GetCurrentMethod().Name;

            return utils.PostData(request, uri, token, requestBody, result, serviceName);

        }

        public static String CelebrityRecognitionToken(String token, String image, String url, float threshold, String endpoint)
        {
            // reuqest data for image tagging
            JObject requestBody = new JObject();
            requestBody.Add("image", image);
            requestBody.Add("url", url);
            requestBody.Add("threshold", threshold);

            HttpWebRequest request = null;
            String result = null;
            String uri = new StringBuilder().Append("https://").Append(endpoint).Append(Ais.CELEBRITY_RECOGNITION).ToString();

            String serviceName = System.Reflection.MethodBase.GetCurrentMethod().Name;

            return utils.PostData(request, uri, token, requestBody, result, serviceName);

        }

        public static String DarkEnhanceToken(String token, String data, float brightness, String endpoint)
        {
            // reuqest data for dark enhance
            JObject requestBody = new JObject();
            requestBody.Add("image", data);
            requestBody.Add("brightness", brightness);

            HttpWebRequest request = null;
            String result = null;
            String uri = new StringBuilder().Append("https://").Append(endpoint).Append(Ais.DARK_ENHANCE).ToString();

            String serviceName = System.Reflection.MethodBase.GetCurrentMethod().Name;

            return utils.PostData(request, uri, token, requestBody, result, serviceName);

        }

        public static String ImageDefogToken(String token, String data, float gamma, bool natural_look, String endpoint)
        {
            // reuqest data for image defog
            JObject requestBody = new JObject();
            requestBody.Add("image", data);
            requestBody.Add("gamma", gamma);
            requestBody.Add("natural_look", natural_look);

            HttpWebRequest request = null;
            String result = null;
            String uri = new StringBuilder().Append("https://").Append(endpoint).Append(Ais.IMAGE_DEFOG).ToString();

            String serviceName = System.Reflection.MethodBase.GetCurrentMethod().Name;

            return utils.PostData(request, uri, token, requestBody, result, serviceName);

        }

        public static String RecaptureDetectToken(String token, String data, String url, float threshold, JArray scene, String endpoint)
        {
            // reuqest data for image defog
            JObject requestBody = new JObject();
            requestBody.Add("image", data);
            requestBody.Add("url", url);
            requestBody.Add("threshold", threshold);
            requestBody.Add("scene", scene);

            HttpWebRequest request = null;
            String result = null;
            String uri = new StringBuilder().Append("https://").Append(endpoint).Append(Ais.RECAPTURE_DETECT).ToString();

            String serviceName = System.Reflection.MethodBase.GetCurrentMethod().Name;

            return utils.PostData(request, uri, token, requestBody, result, serviceName);

        }

        public static String SuperResolutionToken(String token, String data, int scale, String model, String endpoint)
        {
            // reuqest data for image defog
            JObject requestBody = new JObject();
            requestBody.Add("image", data);
            requestBody.Add("scale", scale);
            requestBody.Add("model", model);

            HttpWebRequest request = null;
            String result = null;
            String uri = new StringBuilder().Append("https://").Append(endpoint).Append(Ais.SUPER_RESOLUTION).ToString();

            String serviceName = System.Reflection.MethodBase.GetCurrentMethod().Name;

            return utils.PostData(request, uri, token, requestBody, result, serviceName);

        }

        public static String getEndponit(String region)
        {
            String endpoint = endPointsDic[region];
            return endpoint;
        }
    }
}
