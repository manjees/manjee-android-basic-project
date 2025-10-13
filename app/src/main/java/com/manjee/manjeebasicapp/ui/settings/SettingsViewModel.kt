package com.manjee.manjeebasicapp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manjee.basic.domain.model.AppLanguage
import com.manjee.basic.domain.model.ThemeMode
import com.manjee.basic.domain.usecase.ObserveUserSettingsUseCase
import com.manjee.basic.domain.usecase.SetAppLanguageUseCase
import com.manjee.basic.domain.usecase.SetThemeModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    observeUserSettingsUseCase: ObserveUserSettingsUseCase,
    private val setThemeModeUseCase: SetThemeModeUseCase,
    private val setAppLanguageUseCase: SetAppLanguageUseCase
) : ViewModel() {

    val uiState = observeUserSettingsUseCase()
        .map { settings ->
            SettingsUiState(
                themeMode = settings.themeMode,
                language = settings.language
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SettingsUiState())

    fun onThemeModeSelected(themeMode: ThemeMode) {
        viewModelScope.launch {
            setThemeModeUseCase(themeMode)
        }
    }

    fun onLanguageSelected(language: AppLanguage) {
        viewModelScope.launch {
            setAppLanguageUseCase(language)
        }
    }
}
