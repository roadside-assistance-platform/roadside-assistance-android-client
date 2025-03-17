package esi.roadside.assistance.client.main.presentation.components

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme
import esi.roadside.assistance.client.core.presentation.theme.lightScheme
import esi.roadside.assistance.client.core.presentation.util.isDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: String,
    @DrawableRes background: Int,
    modifier: Modifier = Modifier,
    text: String? = null,
    navigationIcon: @Composable (() -> Unit) = {
        val isDark by isDark()
        val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
        IconButton(
            {
                dispatcher?.onBackPressed()
            },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = if (isDark) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.surface,
                containerColor = Color.Transparent
            )
        ) {
            Icon(Icons.AutoMirrored.Default.ArrowBack, null)
        }

    },
    actions: @Composable (RowScope.() -> Unit) = {},
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val isDark by isDark()
    val colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surfaceContainer).takeIf { isDark }
    Box(
        modifier = modifier.height(180.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painterResource(background),
            null,
            Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            colorFilter = colorFilter
        )
        CenterAlignedTopAppBar(
            {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        text = title,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    text?.let {
                        Text(
                            text = text,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            },
            Modifier,
            navigationIcon,
            actions,
            100.dp,
            windowInsets,
            TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent,
                titleContentColor = lightScheme.background,
            ),
            scrollBehavior
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LargeTopAppBar(
    title: String,
    @DrawableRes background: Int,
    modifier: Modifier = Modifier,
    text: String? = null,
    navigationIcon: @Composable (() -> Unit) = {
        val isDark by isDark()
        val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
        IconButton(
            {
                dispatcher?.onBackPressed()
            },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = if (isDark) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.surface,
                containerColor = Color.Transparent
            )
        ) {
            Icon(Icons.AutoMirrored.Default.ArrowBack, null)
        }

    },
    actions: @Composable (RowScope.() -> Unit) = {},
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val isDark by isDark()
    val colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surfaceContainer).takeIf { isDark }
    Box(
        modifier = modifier.height(300.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painterResource(background),
            null,
            Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            colorFilter = colorFilter
        )
        CenterAlignedTopAppBar(
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = title,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    text?.let {
                        Text(
                            text = text,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            },
            expandedHeight = 200.dp,
            windowInsets = windowInsets,
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent,
                titleContentColor = lightScheme.background,
            ),
            scrollBehavior = scrollBehavior
        )
        Box(Modifier.align(Alignment.TopStart).statusBarsPadding().padding(8.dp)) {
            navigationIcon()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TopAppBarPreview() {
    PreviewAppTheme {
        TopAppBar(
            title = "Notifications",
            text = "You have 3 new notifications",
            background = R.drawable.vector_6
        )
    }
}