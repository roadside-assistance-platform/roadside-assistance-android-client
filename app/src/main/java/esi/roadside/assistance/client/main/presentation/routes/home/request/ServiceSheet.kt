package esi.roadside.assistance.client.main.presentation.routes.home.request

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
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
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme
import esi.roadside.assistance.client.core.presentation.util.isDark
import esi.roadside.assistance.client.main.presentation.Action
import esi.roadside.assistance.client.main.presentation.ClientState
import esi.roadside.assistance.client.main.presentation.components.RatingBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceSheet(
    serviceState: ServiceSheetState,
    clientState: ClientState,
    sheetState: SheetState,
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDark by isDark()
    val colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surfaceContainer).takeIf { isDark }
    var rating by remember { mutableDoubleStateOf(0.0) }
    val textColor =
        if (isDark) contentColorFor(MaterialTheme.colorScheme.surfaceContainer)
        else MaterialTheme.colorScheme.surfaceContainer
    if (clientState !in
        setOf(
            ClientState.IDLE,
            ClientState.ASSISTANCE_IN_PROGRESS,
            ClientState.ASSISTANCE_FAILED
        )
    ) {
        ModalBottomSheet(
            onDismissRequest = {},
            modifier = Modifier,
            sheetState = sheetState,
            dragHandle = null
        ) {
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
                        Row(
                            Modifier.padding(16.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            repeat(3) { index ->
                                AnimatedContent(
                                    clientState.ordinal == (index + 2),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    if (it)
                                        LinearProgressIndicator(
                                            Modifier.fillMaxWidth()
                                        )
                                    else
                                        LinearProgressIndicator(
                                            progress = {
                                                if (clientState.ordinal > (index + 2)) 1f
                                                else 0f
                                            },
                                            Modifier.fillMaxWidth()
                                        )
                                }
                            }
                        }
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
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        AnimatedContent(serviceState.loading) {
                            if (it)
                                LinearProgressIndicator(Modifier.padding(vertical = 30.dp).fillMaxWidth(.5f))
                            else
                                Button(stringResource(R.string.submit_req), Modifier.padding(10.dp)) {
                                    onAction(Action.SubmitRequest)
                                }
                        }
                    }
                }
                item {
                    AnimatedContent(
                        clientState,
                        transitionSpec = {
                            if (initialState > targetState)
                                slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
                            else
                                slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                        }
                    ) {
                        Box(Modifier.fillMaxWidth().height(600.dp)) {
                            when(it) {
                                ClientState.IDLE -> return@Box
                                ClientState.ASSISTANCE_REQUESTED -> return@Box
                                ClientState.PROVIDER_IN_WAY -> {
                                    Column(Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.spacedBy(
                                            8.dp,
                                            Alignment.CenterVertically
                                        ),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(stringResource(R.string.provider_in_way))
                                        Text(stringResource(R.string.provider_in_way_descrption))
                                    }
                                }
                                ClientState.ASSISTANCE_IN_PROGRESS -> {
                                    Column(Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.spacedBy(
                                            8.dp,
                                            Alignment.CenterVertically
                                        ),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(stringResource(R.string.assistance_in_progress))
                                        Text(stringResource(R.string.assistance_in_progress))
                                    }
                                }
                                ClientState.ASSISTANCE_COMPLETED -> {
                                    Column(Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.spacedBy(
                                            8.dp,
                                            Alignment.CenterVertically
                                        ),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ) {
                                        Text(stringResource(R.string.assistance_completed))
                                        Text(stringResource(R.string.assistance_completed_description))
                                        RatingBar(rating, { rating = it })
                                    }
                                }
                                ClientState.ASSISTANCE_FAILED -> return@Box
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