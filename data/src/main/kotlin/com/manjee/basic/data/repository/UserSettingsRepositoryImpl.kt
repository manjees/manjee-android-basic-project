package com.manjee.basic.data.repository

import androidx.datastore.core.DataStore
import com.manjee.basic.data.proto.UserSettingsProto
import com.manjee.basic.domain.model.AppLanguage
import com.manjee.basic.domain.model.ThemeMode
import com.manjee.basic.domain.model.UserSettings
import com.manjee.basic.domain.repository.UserSettingsRepository
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserSettingsRepositoryImpl @Inject constructor(
    private val userSettingsDataStore: DataStore<UserSettingsProto>
) : UserSettingsRepository {

    override fun observeUserSettings(): Flow<UserSettings> {
        val defaultLanguageTag = Locale.getDefault().toLanguageTag()
        return userSettingsDataStore.data.map { proto ->
            proto.toDomain(defaultLanguageTag)
        }
    }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        userSettingsDataStore.updateData { current ->
            current.toBuilder()
                .setThemeMode(themeMode.toProto())
                .build()
        }
    }

    override suspend fun setLanguage(language: AppLanguage) {
        userSettingsDataStore.updateData { current ->
            current.toBuilder()
                .setLanguageTag(language.languageTag)
                .build()
        }
    }

    private fun UserSettingsProto.toDomain(defaultLanguageTag: String): UserSettings {
        val themeMode = when (themeMode) {
            UserSettingsProto.ThemeMode.THEME_MODE_LIGHT -> ThemeMode.LIGHT
            UserSettingsProto.ThemeMode.THEME_MODE_DARK -> ThemeMode.DARK
            UserSettingsProto.ThemeMode.THEME_MODE_UNSPECIFIED,
            UserSettingsProto.ThemeMode.UNRECOGNIZED -> ThemeMode.DARK
        }
        val languageTag = if (languageTag.isNullOrBlank()) defaultLanguageTag else languageTag
        return UserSettings(
            themeMode = themeMode,
            language = AppLanguage.fromTag(languageTag)
        )
    }

    private fun ThemeMode.toProto(): UserSettingsProto.ThemeMode = when (this) {
        ThemeMode.LIGHT -> UserSettingsProto.ThemeMode.THEME_MODE_LIGHT
        ThemeMode.DARK -> UserSettingsProto.ThemeMode.THEME_MODE_DARK
    }
}
