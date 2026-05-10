package com.soma2026.tikitalka.data.service

import com.soma2026.tikitalka.domain.service.TranslationLanguage
import com.soma2026.tikitalka.domain.service.TranslationService

class IosTranslationService : TranslationService {
    override suspend fun translate(text: String, targetLanguage: TranslationLanguage): Result<String> =
        Result.success(text)

    override suspend fun isAvailable(): Boolean = false
}