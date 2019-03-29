#!/usr/bin/python
# -*- coding: utf-8 -*-

class AisEndpoint:
    TTS_ENDPOINT = 'sis.cn-north-1.myhuaweicloud.com'
    ASR_ENDPOINT = 'sis.cn-north-1.myhuaweicloud.com'
    IAM_ENPOINT = 'iam.cn-north-1.myhuaweicloud.com'

class TokenURI:
    AIS_TOKEN = '/v3/auth/tokens'

class ImageURI:
    IMAGE_TAGGING = '/v1.0/image/tagging'
    RECAPTURE_DETECT = '/v1.0/image/recapture-detect'

class ModerationURI:
    IMAGE_ANTI_PORN='/v1.0/moderation/image/anti-porn'
    IMAGE_CLARITY_DETECT='/v1.0/moderation/image/clarity-detect'
    DARK_ENHANCE='/v1.0/vision/dark-enhance'
    DEFOG='/v1.0/vision/defog'
    SURPER_RESOLUTION='/v1.0/vision/super-resolution'

class AsrURI:
    ASR_SENTENCE='/v1.0/voice/asr/sentence'

class TtsURI:
    TTS = '/v1.0/voice/tts'

class AisService:
    IMAGE_SERVICE = 'image'
    MODERATION_SERVICE = 'moderation'
    TTS_SERVICE = 'tts'
    ASR_SERVICE = 'asr'