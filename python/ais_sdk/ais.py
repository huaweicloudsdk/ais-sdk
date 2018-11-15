#!/usr/bin/python
# -*- coding: utf-8 -*-

class AisEndpoint:
    ENDPOINT = 'ais.cn-north-1.myhuaweicloud.com'
    IAM_ENPOINT = 'iam.cn-north-1.myhuaweicloud.com'


class OcrURI:
    OCR_FORM = '/v1.0/ocr/action/ocr_form'
    OCR_VAT_INVOICE = '/v1.0/ocr/vat-invoice'
    OCR_ID_CARD = '/v1.0/ocr/id-card'
    OCR_GENERAL_TABLE = '/v1.0/ocr/general-table'
    OCR_HANDWRITING = '/v1.0/ocr/handwriting'
    OCR_DRIVER_LICENSE = '/v1.0/ocr/driver-license'
    OCR_VEHICLE_LICENSE = '/v1.0/ocr/vehicle-license'
    OCR_BANK_CARD = '/v1.0/ocr/bank-card'


class ImageURI:
    IMAGE_TAGGING = '/v1.0/image/tagging'
    RECAPTURE_DETECT = '/v1.0/image/recapture-detect'


class ModerationURI:
    IMAGE_ANTI_PORN = '/v1.0/moderation/image/anti-porn'
    IMAGE_CLARITY_DETECT = '/v1.0/moderation/image/clarity-detect'
    DARK_ENHANCE = '/v1.0/vision/dark-enhance'
    DEFOG = '/v1.0/vision/defog'
    SURPER_RESOLUTION = '/v1.0/vision/super-resolution'


class AsrURI:
    ASR_SENTENCE = '/v1.0/voice/asr/sentence'


class AisURI:
    ocr_uri = OcrURI
    image_uri = ImageURI
    moderation_uri = ModerationURI
    asr_uri = AsrURI
