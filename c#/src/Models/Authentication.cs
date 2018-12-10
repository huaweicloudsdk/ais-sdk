using System;
using System.Net;
using System.Net.Security;
using System.Security.Cryptography.X509Certificates;
using System.Text;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace Ais.Models
{
    public class Authentication
    {
        public static String GetToken(String username, String domainName, String password, String regionName, String IAM_ENPOINT)
        {
            String requestBodys = RequestBody(username, domainName, password, regionName);
            String url = String.Format("https://{0}{1}", IAM_ENPOINT, Ais.IAM_TOKEN);

            HttpWebRequest request = null;

            try
            {
                ServicePointManager.SecurityProtocol = SecurityProtocolType.Ssl3 | SecurityProtocolType.Tls12 | SecurityProtocolType.Tls11 | SecurityProtocolType.Tls;
                request = WebRequest.Create(url) as HttpWebRequest;
                request.Method = "POST";
                request.ContentType = "application/json";
                byte[] data = Encoding.UTF8.GetBytes(requestBodys);
                request.ContentLength = data.Length;
                using (var stream = request.GetRequestStream())
                {
                    stream.Write(data, 0, data.Length);
                    stream.Close();
                }
                HttpWebResponse reponse = (HttpWebResponse)request.GetResponse();
                return reponse.GetResponseHeader("X-Subject-Token");
            }
            catch (Exception e)
            {
                Console.WriteLine("The service of get token is failed,cause by {0}", e.Message);
                Console.ReadKey();
                return null;

            }
        }

        private static String RequestBody(String username, String domainName, String passwd, String regionName)
        {
            JObject auth = new JObject();

            JObject identity = new JObject();

            JArray methods = new JArray();
            methods.Add("password");
            identity.Add("methods", methods);

            JObject password = new JObject();

            JObject user = new JObject();
            user.Add("name", username);
            user.Add("password", passwd);

            JObject domain = new JObject();
            domain.Add("name", domainName);
            user.Add("domain", domain);

            password.Add("user", user);

            identity.Add("password", password);

            JObject scope = new JObject();

            JObject scopeProject = new JObject();
            scopeProject.Add("name", regionName);

            scope.Add("project", scopeProject);

            auth.Add("identity", identity);
            auth.Add("scope", scope);

            JObject param = new JObject();
            param.Add("auth", auth);
            return JsonConvert.SerializeObject(param);
        }
    }
}
