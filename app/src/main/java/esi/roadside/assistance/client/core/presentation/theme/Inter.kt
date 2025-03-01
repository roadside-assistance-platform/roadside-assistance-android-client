package esi.roadside.assistance.client.core.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import esi.roadside.assistance.client.R

@OptIn(ExperimentalTextApi::class)
val Inter =
    FontFamily(
        Font(
            R.font.inter,
            FontWeight.Normal,
            variationSettings =
                FontVariation.Settings(
                    FontVariation.weight(FontWeight.Normal.weight),
                ),
        ),
        Font(
            R.font.inter,
            FontWeight.SemiBold,
            variationSettings =
                FontVariation.Settings(
                    FontVariation.weight(FontWeight.SemiBold.weight),
                ),
        ),
        Font(
            R.font.inter,
            FontWeight.Medium,
            variationSettings =
                FontVariation.Settings(
                    FontVariation.weight(FontWeight.Medium.weight),
                ),
        ),
        Font(
            R.font.inter,
            FontWeight.Bold,
            variationSettings =
                FontVariation.Settings(
                    FontVariation.weight(FontWeight.Bold.weight),
                ),
        ),
        Font(
            R.font.inter,
            FontWeight.ExtraBold,
            variationSettings =
                FontVariation.Settings(
                    FontVariation.weight(FontWeight.ExtraBold.weight),
                ),
        ),
        Font(
            R.font.inter,
            FontWeight.Black,
            variationSettings =
                FontVariation.Settings(
                    FontVariation.weight(FontWeight.Black.weight),
                ),
        ),
        Font(
            R.font.inter,
            FontWeight.Light,
            variationSettings =
                FontVariation.Settings(
                    FontVariation.weight(FontWeight.Light.weight),
                ),
        ),
        Font(
            R.font.inter,
            FontWeight.ExtraLight,
            variationSettings =
                FontVariation.Settings(
                    FontVariation.weight(FontWeight.ExtraLight.weight),
                ),
        ),
        Font(
            R.font.inter,
            FontWeight.Thin,
            variationSettings =
                FontVariation.Settings(
                    FontVariation.weight(FontWeight.Thin.weight),
                ),
        ),
    )

fun interTypography(typography: Typography) =
    Typography(
        displayLarge =
            typography.displayLarge.copy(
                fontFamily = Inter,
            ),
        displayMedium =
            typography.displayMedium.copy(
                fontFamily = Inter,
            ),
        displaySmall =
            typography.displaySmall.copy(
                fontFamily = Inter,
            ),
        headlineLarge =
            typography.headlineLarge.copy(
                fontFamily = Inter,
            ),
        headlineMedium =
            typography.headlineMedium.copy(
                fontFamily = Inter,
            ),
        headlineSmall =
            typography.headlineSmall.copy(
                fontFamily = Inter,
            ),
        titleLarge =
            typography.titleLarge.copy(
                fontFamily = Inter,
            ),
        titleMedium =
            typography.titleMedium.copy(
                fontFamily = Inter,
            ),
        titleSmall =
            typography.titleSmall.copy(
                fontFamily = Inter,
            ),
        bodyLarge =
            typography.bodyLarge.copy(
                fontFamily = Inter,
            ),
        bodyMedium =
            typography.bodyMedium.copy(
                fontFamily = Inter,
            ),
        bodySmall =
            typography.bodySmall.copy(
                fontFamily = Inter,
            ),
        labelLarge =
            typography.labelLarge.copy(
                fontFamily = Inter,
            ),
        labelMedium =
            typography.labelMedium.copy(
                fontFamily = Inter,
            ),
        labelSmall =
            typography.labelSmall.copy(
                fontFamily = Inter,
            ),
    )
