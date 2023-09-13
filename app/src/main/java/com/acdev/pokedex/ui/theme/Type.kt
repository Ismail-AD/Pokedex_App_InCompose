package com.acdev.pokedex.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.acdev.pokedex.R

// Set of Material typography styles to start with
val Roboto = FontFamily(
    Font(R.font.robotoblack, FontWeight.Black),
    Font(R.font.robotobold, FontWeight.Bold),
    Font(R.font.robotomedium, FontWeight.Medium),
    Font(R.font.robotoregular, FontWeight.Normal)
)

val Poppins = FontFamily(
    Font(R.font.poppins_black, FontWeight.Black),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppinsbold, FontWeight.Bold),
    Font(R.font.poppinssemibold, FontWeight.SemiBold),
    Font(R.font.poppinsregular, FontWeight.Normal)
)

val Monserrat = FontFamily(
    Font(R.font.blackmontserrat, FontWeight.Black),
    Font(R.font.monserratmedium, FontWeight.Medium),
    Font(R.font.montserratregular, FontWeight.Normal)
)
val RobotoCondensed = FontFamily(
    Font(R.font.robotoregular, FontWeight.Normal)
)
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal
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