package com.manjee.manjeebasicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.manjee.manjeebasicapp.ui.navigation.AppNavigation
import androidx.core.view.WindowCompat
import com.manjee.manjeebasicapp.ui.theme.ManjeeBasicAppTheme
import com.manjee.manjeebasicapp.ui.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val settingsUiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(settingsUiState.language) {
                val locales = LocaleListCompat.forLanguageTags(settingsUiState.language.languageTag)
                AppCompatDelegate.setApplicationLocales(locales)
            }

            val isDarkTheme = settingsUiState.themeMode.isDarkTheme()

            ManjeeBasicAppTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        settingsUiState = settingsUiState,
                        onThemeModeChange = settingsViewModel::onThemeModeSelected,
                        onLanguageChange = settingsViewModel::onLanguageSelected
                    )
                }
            }
        }
    }
}
