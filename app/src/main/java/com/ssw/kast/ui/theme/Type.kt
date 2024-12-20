package com.ssw.kast.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ssw.kast.R

val montserrat = FontFamily(
    Font(resId = R.font.montserrat_regular, weight = FontWeight.Normal),
    Font(resId = R.font.montserrat_italic, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(resId = R.font.montserrat_light, weight = FontWeight.Light),
    Font(resId = R.font.montserrat_thin, weight = FontWeight.Thin),
    Font(resId = R.font.montserrat_medium, weight = FontWeight.Medium),
    Font(resId = R.font.montserrat_mediumitalic, weight = FontWeight.Medium, style = FontStyle.Italic),
    Font(resId = R.font.montserrat_semibold, weight = FontWeight.SemiBold),
    Font(resId = R.font.montserrat_bold, weight = FontWeight.Bold),
    Font(resId = R.font.montserrat_black, weight = FontWeight.Black)
)

val notoSans = FontFamily(
    Font(resId = R.font.notosans_regular, weight = FontWeight.Normal),
    Font(resId = R.font.notosans_italic, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(resId = R.font.notosans_light, weight = FontWeight.Light),
    Font(resId = R.font.notosans_bold, weight = FontWeight.Bold),
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = notoSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = notoSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = notoSans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    titleLarge = TextStyle(
        fontFamily = notoSans,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.5.sp
    ),
    titleMedium = TextStyle(
        fontFamily = notoSans,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp
    ),
    labelSmall = TextStyle(
        fontFamily = notoSans,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = notoSans,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = notoSans,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = notoSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)