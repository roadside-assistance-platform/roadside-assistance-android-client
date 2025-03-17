package esi.roadside.assistance.client.settings.presentation

import esi.roadside.assistance.client.R
import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme
import esi.roadside.assistance.client.main.presentation.NavRoutes

data class LargeSettingsGroup(
    @StringRes val label: Int,
    val list: List<LargeSettingsItem>
)

data class LargeSettingsItem(
    @StringRes val title: Int,
    val icon: ImageVector,
    val route: NavRoutes? = null
)

@Composable
fun LargeSettingsItem(
    item: LargeSettingsItem,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    Row(modifier
        .background(MaterialTheme.colorScheme.surfaceContainerHighest)
        .then(
            item.route?.let { route ->
                Modifier.clickable(onClick = {
                    navHostController.navigate(route)
                })
            }
                ?: Modifier
        )
        .padding(12.dp, 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                item.icon,
                null,
                Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(stringResource(item.title), style = MaterialTheme.typography.titleMedium)
        }
    }
}

fun LazyListScope.settingsItem(
    icon: ImageVector?,
    title: Int,
    text: Int,
    visible: Boolean = true,
    onClick: (() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
) {
    item {
        AnimatedVisibility(
            visible = visible,
            label = "",
            enter = fadeIn() + expandVertically(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .then(onClick?.let { Modifier.clickable(onClick = onClick) } ?: Modifier),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(24.dp))
                if (icon == null) {
                    Spacer(Modifier.width(24.dp))
                } else {
                    Icon(icon, null, modifier = Modifier.size(24.dp))
                }
                Spacer(Modifier.width(8.dp))
                Column(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp),
                ) {
                    Text(
                        text = stringResource(title),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        stringResource(text),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
                if (trailingContent != null) trailingContent(this)
            }
        }
    }
}