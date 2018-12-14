using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.IO;
using System.Net;
using System.Net.Security;
using System.Security.Cryptography.X509Certificates;
using System.Text;

namespace Ais.Models
{
    public class utils
    {
        // change file message to base64 String
        public static String ConvertFileToBase64(String filePath)
        {
            System.IO.FileStream fs = System.IO.File.OpenRead(filePath);//传文件的路径即可
            System.IO.BinaryReader br = new BinaryReader(fs);
            byte[] bt = br.ReadBytes(Convert.ToInt32(fs.Length));
            string base64String = Convert.ToBase64String(bt);
            br.Close();
            fs.Close();
            return base64String;
        }

        // change base64 to file
        public static String Base64ToFileAndSave(string base64Str, string filePath)
        {

            try
            {
                byte[] buffer = Convert.FromBase64String(base64Str);
                FileStream fs = new FileStream(Path.GetFullPath(filePath), FileMode.Append);
                fs.Write(buffer, 0, buffer.Length);
                fs.Flush();
                fs.Close();
            }
            catch (Exception ex)
            {
                throw ex;
            }

            return filePath;
        }

        // request data by post
        public static String PostData(HttpWebRequest request, String uri, String token, JObject requestBody, String result, String serviceName)
        {

            try
            {
                ServicePointManager.ServerCertificateValidationCallback = new RemoteCertificateValidationCallback(CheckValidationResult);
                request = WebRequest.Create(uri) as HttpWebRequest;
                request.Method = "POST";
                request.Headers.Add("X-Auth-Token", token);

                request.ContentType = "application/json";
                byte[] data = Encoding.UTF8.GetBytes(JsonConvert.SerializeObject(requestBody));
                request.ContentLength = data.Length;
                using (System.IO.Stream reqStream = request.GetRequestStream())
                {
                    reqStream.Write(data, 0, data.Length);
                    reqStream.Close();
                }
                HttpWebResponse response = (HttpWebResponse)request.GetResponse();

                using (StreamReader reader = new StreamReader(response.GetResponseStream(), Encoding.UTF8))
                {
                    result = reader.ReadToEnd();
                }
                return result;
            }
            catch (WebException e)
            {
                Console.WriteLine("The service of get {0} result is failed,cause by {1}", serviceName, e.Message);
                HttpWebResponse response = (HttpWebResponse)e.Response;
                    using (Stream data = response.GetResponseStream())
                    {
                        using (StreamReader reader = new StreamReader(data))
                        {
                            string text = reader.ReadToEnd();
                            Console.WriteLine(text);
                        }
                    }
                Console.ReadKey();
                return null;
            }
        }

        public static HttpWebResponse getData(HttpWebRequest request, String url, String token, String result, String serviceName)
        {

            try
            {
                ServicePointManager.ServerCertificateValidationCallback = new RemoteCertificateValidationCallback(CheckValidationResult);

                request = (HttpWebRequest)WebRequest.Create(url);
                request.Headers.Add("X-Auth-Token", token);
                request.ContentType = "application/json";

                HttpWebResponse response = (HttpWebResponse)request.GetResponse();

                return response;
            }
            catch (Exception e)
            {
                Console.WriteLine("The service of get {0} function result is failed,cause by {1}", serviceName, e.Message);
                Console.ReadKey();
                return null;
            }
        }


        private static bool CheckValidationResult(object sender, X509Certificate certificate, X509Chain chain, SslPolicyErrors errors)
        {
            return true;
        }

    }
}
