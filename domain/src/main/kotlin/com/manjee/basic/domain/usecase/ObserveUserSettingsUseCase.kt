package com.manjee.basic.domain.usecase

import com.manjee.basic.domain.model.UserSettings
import com.manjee.basic.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUserSettingsUseCase @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) {
    operator fun invoke(): Flow<UserSettings> = userSettingsRepository.observeUserSettings()
}
