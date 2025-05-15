package esi.roadside.assistance.client.main.presentation.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.presentation.InputError
import esi.roadside.assistance.client.core.presentation.components.MyTextField
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme

@Composable
fun InformationCard(
    @StringRes title: Int,
    icon: ImageVector,
    text: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester = remember { FocusRequester() },
    error: InputError? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focus by interactionSource.collectIsFocusedAsState()
    AnimatedContent(enabled) {
        if (it)
            MyTextField(
                value = value,
                onValueChange = {
                    if (enabled) {
                        onValueChange(it)
                    }
                },
                modifier = modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                interactionSource = interactionSource,
                label = stringResource(title),
                leadingIcon = icon,
                error = error
            )
        else
            Card(
                Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                            .size(36.dp)
                            .padding(8.dp),
                        tint =
                            if (focus) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onPrimary
                    )
                    Column(
                        Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = stringResource(title),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium
                            ),
                        )
                    }
                }
            }
    }
}

@Preview
@Composable
private fun InformationCardPreview() {
    var enabled by remember { mutableStateOf(false) }
    PreviewAppTheme {
        InformationCard(
            R.string.email,
            Icons.Default.Email,
            "",
            "",
            {},
            enabled,
        )
    }
}