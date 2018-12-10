using System;
using System.IO;
using System.Net;
using System.Text;
using System.Threading;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace Ais.Models
{
    public class Asr
    {
        public static String AsrBgmToken(String token, String url, String endpoint)
        {
            // reuqest data for asr bgm
            JObject requestBody = new JObject();
            requestBody.Add("url", url);

            HttpWebRequest request = null;
            String result = null;
            String uri = new StringBuilder().Append("https://").Append(endpoint).Append(Ais.ASR_BGM).ToString();

            String serviceName = System.Reflection.MethodBase.GetCurrentMethod().Name;
            return utils.PostData(request, uri, token, requestBody, result, serviceName);

        }

        public static String AsrSentenceToken(String token, String data, String url, String encode_type, String sample_rate, String endpoint)
        {
            // reuqest data for asr scentence
            JObject requestBody = new JObject();
            requestBody.Add("data", data);
            requestBody.Add("url", url);
            requestBody.Add("encode_type", encode_type);
            requestBody.Add("sample_rate", sample_rate);

            HttpWebRequest request = null;
            String result = null;
            String uri = new StringBuilder().Append("https://").Append(endpoint).Append(Ais.ASR_SCENTENCE).ToString();

            String serviceName = System.Reflection.MethodBase.GetCurrentMethod().Name;
            return utils.PostData(request, uri, token, requestBody, result, serviceName);

        }

        public static String LongSentenceToken(String token, String data, String url, String category, String endpoint)
        {
            String jobId = GetJobId(token, data, url, category, endpoint);

            return getResult(endpoint, jobId, token);

        }

        private static String GetJobId(String token, String data, String url, String category, String endpoint)
        {
            // reuqest data for moderation video
            JObject requestBody = new JObject();
            requestBody.Add("data", data);
            requestBody.Add("url", url);
            requestBody.Add("category", category);

            HttpWebRequest request = null;
            String result = null;
            String uri = new StringBuilder().Append("https://").Append(endpoint).Append(Ais.LONG_SENTENCE).ToString();

            String serviceName = System.Reflection.MethodBase.GetCurrentMethod().Name;
            result = utils.PostData(request, uri, token, requestBody, result, serviceName);
            JObject joResult = (JObject)JsonConvert.DeserializeObject(result);

            return joResult["result"]["job_id"].ToString();
        }

        private static String getResult(String endpoint, String jobId, String token)
        {
            String result = null;
            String url = new StringBuilder().Append("https://").Append(endpoint).Append(Ais.LONG_SENTENCE).Append("?job_id=").Append(jobId).ToString();

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
                if ((int)joResult["result"]["status_code"] == -1)
                {
                    break;
                }
                else if ((int)joResult["result"]["status_code"] == 2)
                {
                    result = joResult["result"]["words"].ToString();
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
