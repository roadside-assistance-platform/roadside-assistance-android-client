package esi.roadside.assistance.client.auth.presentation.util

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.presentation.theme.lightScheme
import esi.roadside.assistance.client.core.presentation.util.isDark

@Composable
fun MyScreen(
    title: String,
    text: String,
    modifier: Modifier = Modifier,
    bottomContent: @Composable () -> Unit = {
        TermsAndPolicy(
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .navigationBarsPadding()
        )
    },
    content: @Composable ColumnScope.() -> Unit
) {
    val isDark by isDark()
    val colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surfaceContainer).takeIf { isDark }
    Scaffold(
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(WindowInsets.statusBars),
        bottomBar = bottomContent,
    ) {
        Column(
            modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            Box(Modifier.wrapContentSize()) {
                Image(
                    painter = painterResource(id = R.drawable.welcome_background_4),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(360/384f)
                        .align(Alignment.TopCenter),
                    colorFilter = colorFilter
                )
                Column(
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(360/384f),
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painterResource(R.drawable.app_icon),
                        null,
                        Modifier.size(150.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    AnimatedContent(title, Modifier.fillMaxWidth(), label = "") {
                        Text(
                            text = it,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            color = lightScheme.background
                        )
                    }
                    AnimatedContent(text, Modifier.fillMaxWidth(), label = "") {
                        Text(
                            text = it,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            color = lightScheme.background
                        )
                    }
                }
            }
            content()
        }
    }
}