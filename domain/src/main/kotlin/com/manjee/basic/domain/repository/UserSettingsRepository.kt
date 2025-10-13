package com.manjee.basic.domain.repository

import com.manjee.basic.domain.model.AppLanguage
import com.manjee.basic.domain.model.ThemeMode
import com.manjee.basic.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface UserSettingsRepository {
    fun observeUserSettings(): Flow<UserSettings>
    suspend fun setThemeMode(themeMode: ThemeMode)
    suspend fun setLanguage(language: AppLanguage)
}
