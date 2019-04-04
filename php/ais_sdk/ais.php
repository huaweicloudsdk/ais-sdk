<?php
/**
 * 图像分析服务请求url常量及配置信息
 * Created by PhpStorm.
 * User: Administrator
 * Date: 2018/11/16
 * Time: 10:44
 */

// tts服务请求域名
define("TTS_ENDPOINT", "sis.cn-north-1.myhuaweicloud.com");

// asr服务请求域名
define("ASR_ENDPOINT", "sis.cn-north-1.myhuaweicloud.com");

// token请求域名
define("IAM_ENPOINT", "iam.cn-north-1.myhuaweicloud.com");

// token请求uri
define("AIS_TOKEN", "/v3/auth/tokens");

// 背景音乐识别服务的uri
define("ASR_BGM", "/v1.0/bgm/recognition");

// 图像标签服务uri
define("IMAGE_TAGGING", "/v1.0/image/tagging");

// 图像翻拍检测服务uri
define("RECAPTURE_DETECT", "/v1.0/image/recapture-detect");

// 图像反黄检测服务uri
define("IMAGE_ANTI_PORN", "/v1.0/moderation/image/anti-porn");

// 图像清晰度的检测服务uri
define("IMAGE_CLARITY_DETECT", "/v1.0/moderation/image/clarity-detect");

// 图像低光照增强服务uri
define("DARK_ENHANCE", "/v1.0/vision/dark-enhance");

// 图像去雾服务uri
define("DEFOG", "/v1.0/vision/defog");

// 超分重建服务uri
define("SURPER_RESOLUTION", "/v1.0/vision/super-resolution");

// 语音识别服务uri
define("ASR_SENTENCE", "/v1.0/voice/asr/sentence");

// 语音合成服务uri
define("TTS", "/v1.0/voice/tts");

// 名人识别服务的uri
define("CELEBRITY_RECOGNITION", "/v1.0/image/celebrity-recognition");

// 扭曲校正服务的uri
define("DISTORTION_CORRECT", "/v1.0/moderation/image/distortion-correct");

// 图片内容审核服务的uri
define("IMAGE_CONTENT_DETECT", "/v1.0/moderation/image");

// 长语音识别服务的uri
define("LONG_SENTENCE", "/v1.0/voice/asr/long-sentence");

// 文本内容检测服务的uri
define("MODERATION_TEXT", "/v1.0/moderation/text");

// 视频审核服务的uri
define("MODERATION_VIDEO", "/v1.0/moderation/video");

// 图像内容审核批量异步uri
define("IMAGE_CONTENT_BATCH_JOBS", "/v1.0/moderation/image/batch/jobs");

// 图像内容审核批量异步结果uri
define("IMAGE_CONTENT_BATCH_RESULT", "/v1.0/moderation/image/batch");

// 图片内容审核服务批量的uri
define("IMAGE_CONTENT_BATCH", "/v1.0/moderation/image/batch");

// 图像服务类别
define("IMAGE", "image");

//内容审核服务类别
define("MODERATION", "moderation");