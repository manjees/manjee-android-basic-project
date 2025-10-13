package com.manjee.basic.domain.model

data class UserSettings(
    val themeMode: ThemeMode = ThemeMode.DARK,
    val language: AppLanguage = AppLanguage.fromTag(null)
)
