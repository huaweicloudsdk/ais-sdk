using System;
using System.Net;
using System.Text;
using Newtonsoft.Json.Linq;

namespace Ais.Models
{
    public class TTS
    {
        public static String TTSToken(String token, String text, String voice_name, int volume, String sample_rate, int speech_speed, int pitch_rate, String endpoint)
        {
            // reuqest data for tts
            JObject requestBody = new JObject();
            requestBody.Add("text", text);
            requestBody.Add("voice_name", voice_name);
            requestBody.Add("volume", volume);
            requestBody.Add("sample_rate", sample_rate);
            requestBody.Add("speech_speed", speech_speed);

            HttpWebRequest request = null;
            String result = null;
            String uri = new StringBuilder().Append("https://").Append(endpoint).Append(Ais.TTS).ToString();

            String serviceName = System.Reflection.MethodBase.GetCurrentMethod().Name;

            return utils.PostData(request, uri, token, requestBody, result, serviceName);
        }
    }
}
