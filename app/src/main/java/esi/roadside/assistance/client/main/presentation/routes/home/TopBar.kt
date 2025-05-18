package esi.roadside.assistance.client.main.presentation.routes.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.main.domain.models.geocoding.Feature
import esi.roadside.assistance.client.main.presentation.ClientState
import esi.roadside.assistance.client.main.presentation.routes.home.search.SearchAction
import esi.roadside.assistance.client.main.presentation.routes.home.search.SearchScreen
import esi.roadside.assistance.client.main.presentation.routes.home.search.SearchState
import esi.roadside.assistance.client.main.util.formatTime
import java.time.LocalDateTime
import kotlin.math.roundToLong

@Composable
fun TopBar(
    searchState: SearchState,
    clientState: ClientState,
    time: Long,
    eta: Double?,
    loading: Boolean,
    onAction: (SearchAction) -> Unit,
    onWorkingFinished: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    onItemClick: (Feature) -> Unit,
) {
    val etaTime = eta?.let {
        LocalDateTime.now().apply {
            plusSeconds(eta.roundToLong())
        }
    }
    AnimatedContent(
        clientState in setOf(ClientState.IDLE, ClientState.ASSISTANCE_FAILED, ClientState.ASSISTANCE_COMPLETED),
        modifier,
        contentAlignment = Alignment.TopCenter,
        transitionSpec = {
            (slideInVertically(animationSpec = tween(durationMillis = 300, delayMillis = 200) { -it })
                    togetherWith slideOutVertically { -it })
        }
    ) {
        if (it)
            SearchScreen(
                searchState,
                clientState,
                time,
                onAction,
                onCancel,
                onItemClick = onItemClick
            )
        else {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 4.dp,
            ) {
                Column(
                    Modifier.fillMaxWidth().statusBarsPadding().padding(20.dp),
                    verticalArrangement= Arrangement.spacedBy(16.dp)
                ) {
                    AnimatedContent(
                        clientState,
                        transitionSpec = {
                            if (initialState > targetState)
                                slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
                            else
                                slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                        }
                    ) {
                        Row(Modifier.fillMaxWidth()) {
                            Column(
                                Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = stringResource(it.title),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Medium
                                )
                                val text = when(it) {
                                    ClientState.ASSISTANCE_REQUESTED ->
                                        "Remaining time: ${time.formatTime()}"
                                    ClientState.PROVIDER_IN_WAY ->
                                        "Arrival time: ${etaTime?.formatTime() ?: stringResource(R.string.unknown)}"
                                    ClientState.ASSISTANCE_IN_PROGRESS ->
                                        stringResource(R.string.in_progress_text)
                                    else -> ""
                                }
                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            AnimatedVisibility(clientState == ClientState.ASSISTANCE_REQUESTED) {
                                IconButton(onCancel) {
                                    Icon(Icons.Default.Cancel, null)
                                }
                            }
                        }
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(3) { index ->
                            var progress = when {
                                (clientState.ordinal - 1) > index -> 1f
                                index == 0 -> 1f - (time.toFloat() / (5 * 60 * 1000f))
                                (clientState.ordinal - 1) == index -> 1f
                                else -> 0f
                            }
                            val animatedProgress by animateFloatAsState(
                                targetValue = progress,
                                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
                            )
                            if (clientState.ordinal - 1 == index)
                                AnimatedContent(
                                    loading,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    if (it)
                                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                                    else
                                        LinearProgressIndicator(
                                            progress = { animatedProgress },
                                            modifier = Modifier.fillMaxWidth(),
                                            drawStopIndicator = {}
                                        )
                                }
                            else
                                LinearProgressIndicator(
                                    progress = { animatedProgress },
                                    modifier = Modifier.weight(1f),
                                    drawStopIndicator = {}
                                )
                        }
                    }
                    AnimatedVisibility(
                        (clientState == ClientState.ASSISTANCE_IN_PROGRESS) and (!loading),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onWorkingFinished) {
                            Text(stringResource(R.string.finished))
                        }
                    }
                }
            }
        }
    }
}