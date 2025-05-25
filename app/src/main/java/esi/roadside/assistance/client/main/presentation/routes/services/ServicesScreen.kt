package esi.roadside.assistance.client.main.presentation.routes.services

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.ProvideVicoTheme
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.shader.verticalGradient
import com.patrykandpatrick.vico.compose.m3.common.rememberM3VicoTheme
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shader.ShaderProvider
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme
import esi.roadside.assistance.client.main.domain.models.FetchServicesModel
import esi.roadside.assistance.client.main.domain.models.ServiceModel
import esi.roadside.assistance.client.main.presentation.components.TopAppBar
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen(
    data: FetchServicesModel,
    loading: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    onClick: (ServiceModel) -> Unit = {}
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val modelProducer = remember { CartesianChartModelProducer() }
    val xToDateMapKey = ExtraStore.Key<Map<Float, LocalDate>>()
    val dateTimeFormatter = DateTimeFormatter.ofPattern("d MMM")
    LaunchedEffect(data.data.services) {
        modelProducer.runTransaction {
            val map = data.data.services.groupBy { it.createdAt.toLocalDate() }
            val xToDates = map.keys.associateBy { it.toEpochDay().toFloat() }
            val y = map.keys.sorted().map { date ->
                map[date]?.size ?: 0
            }
            lineSeries { series(xToDates.keys, y) }
            extras { it[xToDateMapKey] = xToDates }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(R.string.history),
                text = "Total requests: ${data.data.services.size}",
                background = R.drawable.vector_6,
                scrollBehavior = scrollBehavior,
                actions = {
                    AnimatedVisibility(!loading) {
                        IconButton(onRefresh, colors = IconButtonDefaults.iconButtonColors(
                            contentColor = Color.White
                        )) {
                            Icon(Icons.Default.Refresh, null)
                        }
                    }
                }
            )
        },
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        AnimatedContent(loading, Modifier) {
            if (it)
                Column(
                    Modifier.fillMaxSize().padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
                ) {
                    Text(
                        stringResource(R.string.loading_services),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                    LinearProgressIndicator(
                        Modifier.fillMaxWidth(.5f),
                    )
                }
            else
                AnimatedContent(data.data.services.isEmpty(), Modifier.fillMaxSize(), label = "") {
                    if (it)
                        EmptyServicesView(Modifier.fillMaxSize())
                    else
                        LazyColumn(
                            Modifier.fillMaxSize().padding(paddingValues),
                            contentPadding = PaddingValues(bottom = 32.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            item {
                                ProvideVicoTheme(rememberM3VicoTheme()) {
                                    val lineColor = MaterialTheme.colorScheme.primary
                                    CartesianChartHost(
                                        rememberCartesianChart(
                                            rememberLineCartesianLayer(
                                                lineProvider =
                                                    LineCartesianLayer.LineProvider.series(
                                                        LineCartesianLayer.rememberLine(
                                                            fill = LineCartesianLayer.LineFill.single(fill(lineColor)),
                                                            areaFill =
                                                                LineCartesianLayer.AreaFill.single(
                                                                    fill(
                                                                        ShaderProvider.verticalGradient(
                                                                            arrayOf(lineColor.copy(alpha = 0.4f),
                                                                                Color.Transparent)
                                                                        )
                                                                    )
                                                                ),
                                                        )
                                                    ),
                                            ),
                                            startAxis = VerticalAxis.rememberStart(),
                                            bottomAxis = HorizontalAxis.rememberBottom(
                                                valueFormatter = CartesianValueFormatter { context, x, _ ->
                                                    (context.model.extraStore.getOrNull(xToDateMapKey)?.get(x.toFloat())
                                                        ?: LocalDate.ofEpochDay(x.toLong())
                                                            )
                                                        .format(dateTimeFormatter)
                                                }
                                            ),
                                        ),
                                        modelProducer,
                                        modifier = Modifier.padding(16.dp),
                                    )
                                }
                            }
                            data.data.services.groupBy { it.createdAt.toLocalDate() }.forEach { (date, services) ->
                                stickyHeader {
                                    Text(
                                        text = date.toString(),
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier
                                            .background(MaterialTheme.colorScheme.background)
                                            .padding(16.dp)
                                            .fillMaxWidth()
                                    )
                                }
                                items(services, { it.id }){
                                    ServiceItem(
                                        service = it,
                                        onClick = {
                                            onClick(it)
                                        },
                                    )
                                }
                            }
                        }
                }
        }
    }
}

@Composable
fun EmptyServicesView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        Image(painterResource(R.drawable.shrug_bro_1), null, Modifier.fillMaxWidth())
        Text(
            stringResource(R.string.no_services),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewAppTheme {
//        ServicesScreen(
//
//        )
    }
}
