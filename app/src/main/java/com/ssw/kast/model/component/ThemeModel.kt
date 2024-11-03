package com.ssw.kast.model.component

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.ssw.kast.model.persistence.PreferencesManager
import com.ssw.kast.ui.theme.DarkGrey
import com.ssw.kast.ui.theme.Darker
import com.ssw.kast.ui.theme.Green
import com.ssw.kast.ui.theme.LighterGreen
import com.ssw.kast.ui.theme.NightSky
import com.ssw.kast.ui.theme.White

class ThemeModel {
    var name: String = ""
    var primary: Color = Green
    var secondary: Color = LighterGreen
    var tertiary: Color = White
    var background: Color = NightSky
    var surface: Color = DarkGrey
    var onPrimary: Color = Darker
    var onSecondary: Color = DarkGrey
    var onTertiary: Color = Green
    var onBackground: Color = White
    var onSurface: Color = White

    constructor()



    constructor(
        name: String,
        primary: Color,
        secondary: Color,
        tertiary: Color,
        background: Color,
        surface: Color,
        onPrimary: Color,
        onSecondary: Color,
        onTertiary: Color,
        onBackground: Color,
        onSurface: Color
    ) {
        this.name = name
        this.primary = primary
        this.secondary = secondary
        this.tertiary = tertiary
        this.background = background
        this.surface = surface
        this.onPrimary = onPrimary
        this.onSecondary = onSecondary
        this.onTertiary = onTertiary
        this.onBackground = onBackground
        this.onSurface = onSurface
    }

    fun toColorScheme(): ColorScheme {
        return lightColorScheme(
            primary = this.primary,
            secondary = this.secondary,
            tertiary = this.tertiary,
            background = this.background,
            surface = this.surface,
            onPrimary = this.onPrimary,
            onSecondary = this.onSecondary,
            onTertiary = this.onTertiary,
            onBackground = this.onBackground,
            onSurface = this.onSurface
        )
    }

    companion object {
        fun switchThemeTo(themeModel: ThemeModel, preferencesManager: PreferencesManager): ColorScheme {
            preferencesManager.saveThemeData(themeModel)
            return themeModel.toColorScheme()
        }

        fun themeList(): List<ThemeModel> {
            val list = listOf(
                ThemeModel(
                    name = "spotify",
                    primary = Green,
                    secondary = LighterGreen,
                    tertiary = White,
                    background = NightSky,
                    surface = DarkGrey,
                    onPrimary = Darker,
                    onSecondary = DarkGrey,
                    onTertiary = Green,
                    onBackground = White,
                    onSurface = White
                ),
                ThemeModel(
                    name = "barbie",
                    primary = Color(0xFF4AA3A2),
                    secondary = Color(0xFFDB6A8F),
                    tertiary = Color(0xFFCA3C66),
                    background = Color(0xFFDADADA),
                    surface = Color(0xFFE8AABE),
                    onPrimary = Color(0xFFDADADA),
                    onSecondary = Color(0xFF4AA3A2),
                    onTertiary = Color(0xFFDADADA),
                    onBackground = Color(0xFFCA3C66),
                    onSurface = Color(0xFF4AA3A2)
                ),
                ThemeModel(
                    name = "chocolate",
                    primary = Color(0xFFF7A391),
                    secondary = Color(0xFFB39188),
                    tertiary = Color(0xFFFFFFFF),
                    background = Color(0xFF392E2C),
                    surface = Color(0xFFF1DCD4),
                    onPrimary = Color(0xFF392E2C),
                    onSecondary = Color(0xFF392E2C),
                    onTertiary = Color(0xFF392E2C),
                    onBackground = Color(0xFFFAF2EA),
                    onSurface = Color(0xFF392E2C)
                ),
                ThemeModel(
                    name = "cold",
                    primary = Color(0xFF0B162C),
                    secondary = Color(0xFF1C2942),
                    tertiary = Color(0xFF5FC2BA),
                    background = Color(0xFF3B556D),
                    surface = Color(0xFFE5EAF0),
                    onPrimary = Color(0xFFFFFFFF),
                    onSecondary = Color(0xFFFFFFFF),
                    onTertiary = Color(0xFF0B162C),
                    onBackground = Color(0xFFFFFFFF),
                    onSurface = Color(0xFF0B162C)
                ),
                ThemeModel(
                    name = "darkOcean",
                    primary = Color(0xFF0794A8),
                    secondary = Color(0xFF709CA7),
                    tertiary = Color(0xFFE1E9EB),
                    background = Color(0xFF344D59),
                    surface = Color(0xFF7A90A4),
                    onPrimary = Color(0xFFB8CBD0),
                    onSecondary = Color(0xFF344D59),
                    onTertiary = Color(0xFF137C8B),
                    onBackground = Color(0xFFB8CBD0),
                    onSurface = Color(0xFF344D59)
                ),
                ThemeModel(
                    name = "meadow",
                    primary = Color(0xFF83EE99),
                    secondary = Color(0xFF59CD97),
                    tertiary = Color(0xFF225B7C),
                    background = Color(0xFFDDFADF),
                    surface = Color(0xFF38A7A6),
                    onPrimary = Color(0xFFFFFFFF),
                    onSecondary = Color(0xFFFFFFFF),
                    onTertiary = Color(0xFF0B162C),
                    onBackground = Color(0xFF0B162C),
                    onSurface = Color(0xFFFFFFFF),
                ),
                ThemeModel(
                    name = "purple",
                    primary = Color(0xFFABA0F9),
                    secondary = Color(0xFFD6CFFF),
                    tertiary = Color(0xFF7C80FC),
                    background = Color(0xFFEBE3F5),
                    surface = Color(0xFFFEFEFF),
                    onPrimary = Color(0xFFFFFFFF),
                    onSecondary = Color(0xFF7C80FC),
                    onTertiary = Color(0xFFFFFFFF),
                    onBackground = Color(0xFF7C80FC),
                    onSurface = Color(0xFF7C80FC)
                ),
                ThemeModel(
                    name = "silver",
                    primary = Color(0xFF8A8A8A),
                    secondary = Color(0xFF7E7E7E),
                    tertiary = Color(0xFF585858),
                    background = Color(0xFFF0F0F0),
                    surface = Color(0xFFCACACA),
                    onPrimary = Color(0xFFFFFFFF),
                    onSecondary = Color(0xFFFFFFFF),
                    onTertiary = Color(0xFFFFFFFF),
                    onBackground = Color(0xFF585858),
                    onSurface = Color(0xFF585858)
                ),
                ThemeModel(
                    name = "vacation",
                    primary = Color(0xFFEA5863),
                    secondary = Color(0xFFFE9063),
                    tertiary = Color(0xFF27C7D4),
                    background = Color(0xFFFDF0E7),
                    surface = Color(0xFFFFFAF7),
                    onPrimary = Color(0xFFFFFFFF),
                    onSecondary = Color(0xFFFFFFFF),
                    onTertiary = Color(0xFFFFFFFF),
                    onBackground = Color(0xFF27C7D4),
                    onSurface = Color(0xFFFDF0E7)
                ),
                ThemeModel(
                    name = "woodie",
                    primary = Color(0xFFF1895C),
                    secondary = Color(0xFF2E3244),
                    tertiary = Color(0xFF516079),
                    background = Color(0xFFFFFFFF),
                    surface = Color(0xFFC5C6C6),
                    onPrimary = Color(0xFF516079),
                    onSecondary = Color(0xFFFFFFFF),
                    onTertiary = Color(0xFFF1895C),
                    onBackground = Color(0xFF2E3244),
                    onSurface = Color(0xFFF1895C)
                )
            )
            return list
        }
    }

}