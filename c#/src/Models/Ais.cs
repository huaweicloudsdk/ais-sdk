using System;


namespace Ais.Models
{
    public class Ais
    {
        // the uri for get token 
        public static String IAM_TOKEN = "/v3/auth/tokens";

        // the uri for Service image tagging 
        public static String IMAGE_TAGGING = "/v1.0/image/tagging";

        // the uri for Service asr bgm 
        public static String ASR_BGM = "/v1.0/bgm/recognition";

        // the uri for Service asr scentence 
        public static String ASR_SCENTENCE = "/v1.0/voice/asr/sentence";

        // the uri for Service celebrity recognition
        public static String CELEBRITY_RECOGNITION = "/v1.0/image/celebrity-recognition";

        // the uri for Service clarity detect
        public static String IMAGE_CLARITY_DETECT = "/v1.0/moderation/image/clarity-detect";

        // the uri for Service dark enhance
        public static String DARK_ENHANCE = "/v1.0/vision/dark-enhance";

        // the uri for Service distortion correction
        public static String DISTORTION_CORRECTION = "/v1.0/moderation/image/distortion-correct";

        // the uri for Service image anti porn
        public static String IMAGE_ANTI_PORN = "/v1.0/moderation/image/anti-porn";

        // the uri for Service image content
        public static String IMAGE_CONTENT = "/v1.0/moderation/image";

        // the uri for Service image defog
        public static String IMAGE_DEFOG = "/v1.0/vision/defog";

        // the uri for Service moderation text
        public static String MODERATION_TEXT = "/v1.0/moderation/text";

        // the uri for Service recapture detect
        public static String RECAPTURE_DETECT = "/v1.0/image/recapture-detect";

        // the uri for Service super resolution
        public static String SUPER_RESOLUTION = "/v1.0/vision/super-resolution";

        // the uri for tts
        public static String TTS = "/v1.0/voice/tts";

        // the uri for service of moderation video
        public static String MODERATION_VIDEO = "/v1.0/moderation/video";

        // the uri for service of long sentence
        public static String LONG_SENTENCE = "/v1.0/voice/asr/long-sentence";

        // the uri for service of image content batch result
        public static String IMAGE_CONTENT_BATCH_RESULT = "/v1.0/moderation/image/batch";

        // the uri for service of image content batch job id
        public static String IMAGE_CONTENT_BATCH_JOBS = "/v1.0/moderation/image/batch/jobs";

        // the uri for service of image content batch result
        public static String IMAGE_CONTENT_BATCH = "/v1.0/moderation/image/batch";
    }
}
