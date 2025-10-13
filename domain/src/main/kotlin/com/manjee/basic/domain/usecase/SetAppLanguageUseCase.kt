package com.manjee.basic.domain.usecase

import com.manjee.basic.domain.model.AppLanguage
import com.manjee.basic.domain.repository.UserSettingsRepository
import javax.inject.Inject

class SetAppLanguageUseCase @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) {
    suspend operator fun invoke(language: AppLanguage) {
        userSettingsRepository.setLanguage(language)
    }
}
