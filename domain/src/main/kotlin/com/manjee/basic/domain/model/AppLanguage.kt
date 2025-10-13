package com.manjee.basic.domain.model

import java.util.Locale

enum class AppLanguage(val languageTag: String) {
    ENGLISH("en"),
    KOREAN("ko");

    companion object {
        fun fromTag(tag: String?): AppLanguage {
            if (tag.isNullOrBlank()) return fromTag(Locale.getDefault().toLanguageTag())
            val normalizedTag = tag.lowercase(Locale.ROOT)
            return entries.firstOrNull { normalizedTag.startsWith(it.languageTag) } ?: ENGLISH
        }
    }
}
