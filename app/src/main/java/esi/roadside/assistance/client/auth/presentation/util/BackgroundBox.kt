package esi.roadside.assistance.client.auth.presentation.util

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.core.presentation.util.isDark
import esi.roadside.assistance.client.main.presentation.components.DefaultBackNavButton

@Composable
fun BackgroundBox(
    @DrawableRes
    resource: Int,
    modifier: Modifier = Modifier,
    navigationButton: @Composable BoxScope.() -> Unit = {
        DefaultBackNavButton(Modifier
            .align(Alignment.TopStart)
            .statusBarsPadding()
            .offset(12.dp, 12.dp)
        )
    },
    content: @Composable BoxScope.() -> Unit
) {
    val isDark by isDark()
    val colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surfaceContainer).takeIf { isDark }
    Surface(modifier = modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = resource),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                colorFilter = colorFilter
            )
            navigationButton()
            content()
        }
    }
}