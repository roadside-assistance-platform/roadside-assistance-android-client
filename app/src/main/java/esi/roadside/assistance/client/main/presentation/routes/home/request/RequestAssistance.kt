package esi.roadside.assistance.client.main.presentation.routes.home.request

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import esi.roadside.assistance.client.auth.presentation.util.ToggleOutlineButton
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme
import esi.roadside.assistance.client.core.presentation.theme.lightScheme
import esi.roadside.assistance.client.core.presentation.util.isDark
import esi.roadside.assistance.client.main.domain.Categories
import esi.roadside.assistance.client.main.presentation.Action
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestAssistance(
    sheetState: SheetState,
    state: RequestAssistanceState,
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDark by isDark()
    val colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surfaceContainer).takeIf { isDark }
    val background =
        if (isDark) MaterialTheme.colorScheme.surfaceContainer
        else Color(323232)
    val textColor =
        if (isDark) contentColorFor(MaterialTheme.colorScheme.surfaceContainer)
        else MaterialTheme.colorScheme.surfaceContainer
    if (state.sheetVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                onAction(Action.HideRequestAssistance)
            },
            modifier = Modifier,
            sheetState = sheetState,
            dragHandle = {
                Box(Modifier.background(background)) {
                    BottomSheetDefaults.DragHandle(color = textColor)
                }
            }
        ) {
            LazyColumn(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Box(
                        modifier = modifier.height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painterResource(R.drawable.union),
                            null,
                            Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds,
                            colorFilter = colorFilter
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
                                        { onAction(Action.SelectCategory(category)) },
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
                        onValueChange = { onAction(Action.SetDescription(it)) },
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
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Button(stringResource(R.string.submit_req), Modifier.padding(10.dp)) {
                            onAction(Action.SubmitRequest)
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