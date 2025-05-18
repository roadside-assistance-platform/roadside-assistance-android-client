package esi.roadside.assistance.client.main.presentation.sheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.presentation.util.launchCallIntent
import esi.roadside.assistance.client.main.domain.models.ProviderInfo
import esi.roadside.assistance.client.main.presentation.Action
import esi.roadside.assistance.client.main.presentation.components.MyImage
import esi.roadside.assistance.client.main.util.plus

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NavigatingScreen(
    provider: ProviderInfo?,
    message: String,
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val isKeyboardVisible = WindowInsets.isImeVisible
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    val isFocused by remember {
        derivedStateOf { focused || isKeyboardVisible }
    }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    LazyColumn(
        modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 16.dp) + WindowInsets.navigationBars.asPaddingValues(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        provider?.let {
            item {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MyImage(
                        model = provider.photo,
                        icon = Icons.Default.Person,
                        modifier = Modifier.size(60.dp),
                        shape = CircleShape,
                    )
                    Column(
                        Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            provider.fullName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                        )
                        Text(
                            provider.phone,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            item {
                Row(
                    Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AnimatedVisibility(!isFocused) {
                        FilledIconButton({ context.launchCallIntent(provider.phone) }) {
                            Icon(Icons.Default.Phone, null)
                        }
                    }
                    OutlinedTextField(
                        value = message,
                        onValueChange = { onAction(Action.SetMessage(it)) },
                        shape = RoundedCornerShape(100),
                        modifier = Modifier.weight(1f).focusRequester(focusRequester),
                        interactionSource = interactionSource,
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        ),
                        placeholder = {
                            Text(
                                stringResource(R.string.chat_placeholder),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                {
                                    { onAction(Action.SendMessage) }
                                    focusManager.clearFocus(true)
                                },
                                modifier = Modifier.padding(end = 2.dp),
                                enabled = message.isNotBlank(),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor =
                                        if (isFocused) MaterialTheme.colorScheme.primary
                                        else Color.Unspecified,
                                    contentColor =
                                        if (isFocused) MaterialTheme.colorScheme.onPrimary
                                        else Color.Unspecified,
                                    disabledContainerColor = Color.Transparent,
                                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                ),
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Send, null)
                            }
                        },
                    )
                }
            }
        }
    }
}