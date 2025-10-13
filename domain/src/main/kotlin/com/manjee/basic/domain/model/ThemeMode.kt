package com.manjee.basic.domain.model

enum class ThemeMode {
    LIGHT,
    DARK;

    companion object {
        fun from(isDarkTheme: Boolean): ThemeMode = if (isDarkTheme) DARK else LIGHT
    }

    fun isDarkTheme(): Boolean = this == DARK
}
