package esi.roadside.assistance.client.main.presentation.routes.home.request

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.presentation.util.Button
import esi.roadside.assistance.client.auth.presentation.util.OutlinedButton
import esi.roadside.assistance.client.auth.presentation.util.ToggleOutlineButton
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme
import esi.roadside.assistance.client.core.presentation.util.isDark
import esi.roadside.assistance.client.main.domain.Categories
import esi.roadside.assistance.client.main.domain.services.VehicleIssueAIService
import org.koin.compose.getKoin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestAssistance(
    sheetState: SheetState,
    state: RequestAssistanceState,
    onAction: (AssistanceAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDark by isDark()
    val colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surfaceContainer).takeIf { isDark }
    val textColor =
        if (isDark) contentColorFor(MaterialTheme.colorScheme.surfaceContainer)
        else MaterialTheme.colorScheme.surfaceContainer
    //val vehicleIssueAIService = getKoin().get<VehicleIssueAIService>()
    if (state.sheetVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                onAction(AssistanceAction.HideSheet)
            },
            modifier = Modifier,
            sheetState = sheetState,
            dragHandle = null,
            contentWindowInsets = { WindowInsets(0, 0, 0, 0) }
        ) {
            AnimatedContent(state.isAIDetectionActive) {
                if (it)
                    AIDetectionScreen(
                        { uri, audio ->
                            onAction(AssistanceAction.SubmitAIData(uri, audio))
                        },
                    ) {
                        onAction(AssistanceAction.CloseAIDetection)
                    }
                else
                    LazyColumn(
                        Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Box(
                                modifier = modifier.height(240.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painterResource(R.drawable.union),
                                    null,
                                    Modifier.fillMaxSize(),
                                    contentScale = ContentScale.FillBounds,
                                    colorFilter = colorFilter
                                )
                                BottomSheetDefaults.DragHandle(
                                    color = textColor,
                                    modifier = Modifier.align(Alignment.TopCenter)
                                )
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.offset(y = (-20).dp)
                                ) {
                                    Text(
                                        text = stringResource(R.string.request_assistance),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = textColor
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = stringResource(R.string.request_assistance_des),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = textColor
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                        item {
                            Text(
                                text = stringResource(R.string.selecte_the_field),
                                style = MaterialTheme.typography.titleSmall,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        item {
                            Column(
                                Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Categories.entries.chunked(2).forEach {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        it.forEach { category ->
                                            ToggleOutlineButton(
                                                state.category == category,
                                                { onAction(AssistanceAction.SelectCategory(category)) },
                                                Modifier.weight(1f)
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = category.icon),
                                                    contentDescription = null,
                                                    modifier = Modifier.size(ButtonDefaults.IconSize)
                                                )
                                                Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                                                Text(
                                                    text = stringResource(category.text),
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        item {
                            Text(
                                text = stringResource(R.string.des_issue),
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                        item {
                            BasicTextField(
                                value = state.description,
                                onValueChange = { onAction(AssistanceAction.SetDescription(it)) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .padding(horizontal = 16.dp)
                                    .clip(MaterialTheme.shapes.large)
                                    .background(MaterialTheme.colorScheme.surfaceContainer),
                                textStyle = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                                decorationBox = { innerTextField ->
                                    Box(Modifier
                                        .fillMaxSize()
                                        .padding(16.dp)) {
                                        if (state.description.isEmpty())
                                            Text(
                                                stringResource(R.string.description_issue_placeholder),
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            )
                                        innerTextField()
                                    }
                                }
                            )
                        }
                        item {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .navigationBarsPadding()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                AnimatedContent(state.loading) {
                                    if (it)
                                        LinearProgressIndicator(Modifier.padding(vertical = 30.dp).fillMaxWidth(.5f))
                                    else
                                        Row(
                                            Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            OutlinedButton(
                                                stringResource(R.string.use_ai),
                                                Modifier.weight(1f),
                                                icon = Icons.Default.AutoFixHigh
                                            ) {
                                                onAction(AssistanceAction.StartAIDetection)
                                            }
                                            Button(
                                                stringResource(R.string.submit),
                                                Modifier.weight(1f),
                                                icon = Icons.AutoMirrored.Filled.Send
                                            ) {
                                                onAction(AssistanceAction.Submit)
                                            }
                                        }
                                }
                            }
                        }
                    }
                }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun RequestAssistancePreview() {
    PreviewAppTheme {
        RequestAssistance(
            sheetState = rememberModalBottomSheetState(),
            state = RequestAssistanceState(),
            onAction = {},
        )
    }
}
