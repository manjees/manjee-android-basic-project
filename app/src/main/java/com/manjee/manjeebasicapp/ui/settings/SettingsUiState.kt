package com.manjee.manjeebasicapp.ui.settings

import com.manjee.basic.domain.model.AppLanguage
import com.manjee.basic.domain.model.ThemeMode

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.DARK,
    val language: AppLanguage = AppLanguage.fromTag(null)
)
