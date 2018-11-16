/**
 * 图像分析服务类请求url常量配置信息
 * @type
 */
var ais = {
    // 服务请求域名
    ENDPOINT: 'ais.cn-north-1.myhuaweicloud.com',

    // token请求域名
    IAM_ENPOINT: 'iam.cn-north-1.myhuaweicloud.com',

    // token请求uri
    AIS_TOKEN: '/v3/auth/tokens',

    // 背景音乐识别服务的uri
    ASR_BGM: '/v1.0/bgm/recognition',

    // 图像标签服务uri
    IMAGE_TAGGING: '/v1.0/image/tagging',

    // 图像翻拍检测服务uri
    RECAPTURE_DETECT: '/v1.0/image/recapture-detect',

    // 图像反黄检测服务uri
    IMAGE_ANTI_PORN: '/v1.0/moderation/image/anti-porn',

    // 图像清晰度的检测服务uri
    IMAGE_CLARITY_DETECT: '/v1.0/moderation/image/clarity-detect',

    // 图像低光照增强服务uri
    DARK_ENHANCE: '/v1.0/vision/dark-enhance',

    // 图像去雾服务uri
    DEFOG: '/v1.0/vision/defog',

    // 超分重建服务uri
    SURPER_RESOLUTION: '/v1.0/vision/super-resolution',

    // 语音识别服务uri
    ASR_SENTENCE: '/v1.0/voice/asr/sentence',

    // 语音合成服务uri
    TTS: '/v1.0/voice/tts',

    // 名人识别服务的uri
    CELEBRITY_RECOGNITION: '/v1.0/image/celebrity-recognition',

    // 扭曲校正服务的uri
    DISTORTION_CORRECT: '/v1.0/moderation/image/distortion-correct',

    // 图片内容审核服务的uri
    IMAGE_CONTENT_DETECT: '/v1.0/moderation/image',

    // 长语音识别服务的uri
    LONG_SENTENCE: '/v1.0/voice/asr/long-sentence',

    // 文本内容检测服务的uri
    MODERATION_TEXT: '/v1.0/moderation/text',

    // 视频审核服务的uri
    MODERATION_VIDEO: '/v1.0/moderation/video'
};
module.exports = ais;