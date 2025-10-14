package com.manjee.manjeebasicapp

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.os.LocaleListCompat
import androidx.core.text.TextUtilsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.manjee.manjeebasicapp.ui.navigation.AppNavigation
import androidx.core.view.WindowCompat
import com.manjee.manjeebasicapp.ui.theme.ManjeeBasicAppTheme
import com.manjee.manjeebasicapp.ui.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

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
            val baseContext = LocalContext.current
            val locale = settingsUiState.language.toLocale()
            val localizedContext = rememberLocalizedContext(baseContext, locale)
            val layoutDirection = rememberLayoutDirection(locale)

            CompositionLocalProvider(
                LocalContext provides localizedContext,
                LocalLayoutDirection provides layoutDirection
            ) {
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
}

@Composable
private fun rememberLocalizedContext(baseContext: Context, locale: Locale): Context {
    return remember(baseContext, locale) {
        val configuration = Configuration(baseContext.resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale)
        } else {
            @Suppress("DEPRECATION")
            configuration.locale = locale
        }
        baseContext.createConfigurationContext(configuration)
    }
}

@Composable
private fun rememberLayoutDirection(locale: Locale): LayoutDirection {
    return remember(locale) {
        val direction = TextUtilsCompat.getLayoutDirectionFromLocale(locale)
        if (direction == View.LAYOUT_DIRECTION_RTL) LayoutDirection.Rtl else LayoutDirection.Ltr
    }
}
