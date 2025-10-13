package com.manjee.basic.domain.usecase

import com.manjee.basic.domain.model.ThemeMode
import com.manjee.basic.domain.repository.UserSettingsRepository
import javax.inject.Inject

class SetThemeModeUseCase @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) {
    suspend operator fun invoke(themeMode: ThemeMode) {
        userSettingsRepository.setThemeMode(themeMode)
    }
}
