package core

const (
	// domain name for asr service
	ASR_ENDPOINT string = "sis.cn-north-1.myhuaweicloud.com"
	
	// domain name for tts service 
	TTS_ENDPOINT string = "sis.cn-north-1.myhuaweicloud.com"

	// domain name for get token
	IAM_ENPOINT string = "iam.cn-north-1.myhuaweicloud.com"

	// the uri for get token
	IAM_TOKEN string = "/v3/auth/tokens"

	// instrument recognition uri
	INSTRUMENT = "/v1.0/image/classify/instrument"

	// the uri for Service image tagging
	IMAGE_TAGGING string = "/v1.0/image/tagging"

	// the uri for Service asr bgm
	ASR_BGM string = "/v1.0/bgm/recognition"

	// the uri for Service asr scentence
	ASR_SCENTENCE string = "/v1.0/voice/asr/sentence"

	// the uri for Service celebrity recognition
	CELEBRITY_RECOGNITION string = "/v1.0/image/celebrity-recognition"

	// the uri for Service clarity detect
	IMAGE_CLARITY_DETECT string = "/v1.0/moderation/image/clarity-detect"

	// the uri for Service dark enhance
	DARK_ENHANCE string = "/v1.0/vision/dark-enhance"

	// the uri for Service distortion correction
	DISTORTION_CORRECTION string = "/v1.0/moderation/image/distortion-correct"

	// the uri for Service image anti porn
	IMAGE_ANTI_PORN string = "/v1.0/moderation/image/anti-porn"

	// the uri for Service image content
	IMAGE_MODERATION string = "/v1.0/moderation/image"

	// the uri for Service image defog
	IMAGE_DEFOG string = "/v1.0/vision/defog"

	// the uri for Service moderation text
	MODERATION_TEXT string = "/v1.0/moderation/text"

	// the uri for Service recapture detect
	RECAPTURE_DETECT string = "/v1.0/image/recapture-detect"

	// the uri for Service super resolution
	SUPER_RESOLUTION string = "/v1.0/vision/super-resolution"

	// the uri for tts
	TTS string = "/v1.0/voice/tts"

	// the uri for service of moderation video
	MODERATION_VIDEO string = "/v1.0/moderation/video"

	// the uri for service of long sentence
	LONG_SENTENCE string = "/v1.0/voice/asr/long-sentence"

	// the uri for service of image content batch result
	IMAGE_MODERATION_BATCH_RESULT string = "/v1.0/moderation/image/batch"

	// the uri for service of image content batch job id
	IMAGE_MODERATION_BATCH_JOBS string = "/v1.0/moderation/image/batch/jobs"

	// the uri for service of image content batch result
	IMAGE_MODERATION_BATCH string = "/v1.0/moderation/image/batch"
	
	// image service type 
	IMAGE = "image"
	
	// moderation service type 
	MODERATION = "moderation"
	
	// the max retyr tiems
	RETRY_MAX_TIMES int = 3
)
