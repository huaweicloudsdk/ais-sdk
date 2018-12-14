using System;
using System.IO;
using System.Net;
using System.Text;
using System.Threading;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace Ais.Models
{
    public class Moderation
    {
        public static String ClarityDetectToken(String token, String image, String url, float threshold, String endpoint)
        {
            // reuqest data for image clarity detect
            JObject requestBody = new JObject();
            requestBody.Add("image", image);
            requestBody.Add("url", url);
            requestBody.Add("threshold", threshold);

            HttpWebRequest request = null;
            String result = null;
            String uri = new StringBuilder().Append("https://").Append(endpoint).Append(Ais.IMAGE_CLARITY_DETECT).ToString();

            String serviceName = System.Reflection.MethodBase.GetCurrentMethod().Name;

            return utils.PostData(request, uri, token, requestBody, result, serviceName);

        }

        public static String DistortionCorrectToken(String token, String image, String url, bool correction, String endpoint)
        {
            // reuqest data for distortion correct
            JObject requestBody = new JObject();
            requestBody.Add("image", image);
            requestBody.Add("url", url);
            requestBody.Add("correction", correction);

            HttpWebRequest request = null;
            String result = null;
            String uri = new StringBuilder().Append("https://").Append(endpoint).Append(Ais.DISTORTION_CORRECTION).ToString();

            String serviceName = System.Reflection.MethodBase.GetCurrentMethod().Name;

            return utils.PostData(request, uri, token, requestBody, result, serviceName);

        }

        public static String AntiPornToken(String token, String image, String url, String endpoint)
        {
            // reuqest data for image anti porn
            JObject requestBody = new JObject();
            requestBody.Add("image", image);
            requestBody.Add("url", url);

            HttpWebRequest request = null;
            String result = null;
            String uri = new StringBuilder().Append("https://").Append(endpoint).Append(Ais.IMAGE_ANTI_PORN).ToString();

            String serviceName = System.Reflection.MethodBase.GetCurrentMethod().Name;

            return utils.PostData(request, uri, token, requestBody, result, serviceName);

        }

        public static String ImageContentToken(String token, String image, String url, float threshold, JArray categories, String endpoint)
        {
            // reuqest data for image content detect
            JObject requestBody = new JObject();
            requestBody.Add("image", image);
            requestBody.Add("url", url);
            requestBody.Add("categories", categories);
            requestBody.Add("threshold", threshold);

            HttpWebRequest request = null;
            String result = null;
            String uri = new StringBuilder().Append("https://").Append(endpoint).Append(Ais.IMAGE_CONTENT).ToString();

            String serviceName = System.Reflection.MethodBase.GetCurrentMethod().Name;

            return utils.PostData(request, uri, token, requestBody, result, serviceName);

        }

        public static String ImageContentBatchToken(String token, JArray urls, float threshold, JArray categories, String endpoint)
        {
            // reuqest data for image content detect
            JObject requestBody = new JObject();
            requestBody.Add("urls", urls);
            requestBody.Add("categories", categories);
            requestBody.Add("threshold", threshold);

            HttpWebRequest request = null;
            String result = null;
            String uri = new StringBuilder().Append("https://").Append(endpoint).Append(Ais.IMAGE_CONTENT_BATCH).ToString();

            String serviceName = System.Reflection.MethodBase.GetCurrentMethod().Name;

            return utils.PostData(request, uri, token, requestBody, result, serviceName);

        }

        public static String ModerationTextToken(String token, JArray categories, JArray items, String endpoint)
        {
            // reuqest data for moderation text
            JObject requestBody = new JObject();
            requestBody.Add("categories", categories);
            requestBody.Add("items", items);

            HttpWebRequest request = null;
            String result = null;
            String uri = new StringBuilder().Append("https://").Append(endpoint).Append(Ais.MODERATION_TEXT).ToString();

            String serviceName = System.Reflection.MethodBase.GetCurrentMethod().Name;

            return utils.PostData(request, uri, token, requestBody, result, serviceName);

        }


        public static String VideoToken(String token, String url, int frame_interval, JArray categories, String endpoint)
        {
            String jobId = GetJobId(token, url, frame_interval, categories, endpoint);

            return getResult(endpoint, jobId, token);

        }

        private static String GetJobId(String token, String url, int frame_interval, JArray categories, String endpoint)
        {
            // reuqest data for moderation video
            JObject requestBody = new JObject();
            requestBody.Add("categories", categories);
            requestBody.Add("frame_interval", frame_interval);
            requestBody.Add("url", url);

            HttpWebRequest request = null;
            String result = null;
            String uri = new StringBuilder().Append("https://").Append(endpoint).Append(Ais.MODERATION_VIDEO).ToString();

            String serviceName = System.Reflection.MethodBase.GetCurrentMethod().Name;
            result = utils.PostData(request, uri, token, requestBody, result, serviceName);
            JObject joResult = (JObject)JsonConvert.DeserializeObject(result);

            return joResult["result"]["job_id"].ToString();
        }

        private static String getResult(String endpoint, String jobId, String token)
        {
            String result = null;
            String url = new StringBuilder().Append("https://").Append(endpoint).Append(Ais.MODERATION_VIDEO).Append("?job_id=").Append(jobId).ToString();

            HttpWebRequest request = null;
            String serviceName = System.Reflection.MethodBase.GetCurrentMethod().Name;

            while (true)
            {
                HttpWebResponse response = utils.getData(request, url, token, result, serviceName);
                int httpStatus = (int)response.StatusCode;
                if (httpStatus != 200)
                {
                    break;
                }
                result = new StreamReader(response.GetResponseStream()).ReadToEnd();

                JObject joResult = (JObject)JsonConvert.DeserializeObject(result);
                if (joResult["result"]["status"].ToString() == "failed" || joResult["result"]["status"].ToString() == "finish")
                {
                    break;
                }
                else
                {
                    Thread.Sleep(3000);
                    continue;
                }

            }
            return result;

        }

        public static string ImageContentBatchJobsToken(string token, JArray urls, JArray categories, string endpoint)
        {
    
            String jobId = GetImageContentJobId(token, urls, categories, endpoint);

            return getImageContentResult(endpoint, jobId, token);
        }


        private static String GetImageContentJobId(String token, JArray urls, JArray categories, String endpoint)
        {
            // reuqest data for iamge content of batch jobs
            JObject requestBody = new JObject();
            requestBody.Add("categories", categories);
            requestBody.Add("urls", urls);

            HttpWebRequest request = null;
            String result = null;
            String uri = new StringBuilder().Append("https://").Append(endpoint).Append(Ais.IMAGE_CONTENT_BATCH_JOBS).ToString();

            String serviceName = System.Reflection.MethodBase.GetCurrentMethod().Name;
            result = utils.PostData(request, uri, token, requestBody, result, serviceName);
            JObject joResult = (JObject)JsonConvert.DeserializeObject(result);

            return joResult["result"]["job_id"].ToString();
        }

        private static String getImageContentResult(String endpoint, String jobId, String token)
        {
            String result = null;
            String url = new StringBuilder().Append("https://").Append(endpoint).Append(Ais.IMAGE_CONTENT_BATCH_RESULT).Append("?job_id=").Append(jobId).ToString();

            HttpWebRequest request = null;
            String serviceName = System.Reflection.MethodBase.GetCurrentMethod().Name;

            while (true)
            {
                HttpWebResponse response = utils.getData(request, url, token, result, serviceName);
                int httpStatus = (int)response.StatusCode;
                if (httpStatus != 200)
                {
                    break;
                }
                result = new StreamReader(response.GetResponseStream()).ReadToEnd();

                JObject joResult = (JObject)JsonConvert.DeserializeObject(result);
                if (joResult["result"]["status"].ToString() == "failed" || joResult["result"]["status"].ToString() == "finish")
                {
                    break;
                }
                else
                {
                    Thread.Sleep(3000);
                    continue;
                }

            }
            return result;

        }
    }
}
