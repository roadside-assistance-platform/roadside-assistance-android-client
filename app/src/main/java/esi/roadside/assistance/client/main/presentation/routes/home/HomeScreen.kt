package esi.roadside.assistance.client.main.presentation.routes.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.TransitionOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.extension.compose.style.BooleanValue
import com.mapbox.maps.extension.compose.style.ColorValue
import com.mapbox.maps.extension.compose.style.DoubleListValue
import com.mapbox.maps.extension.compose.style.DoubleValue
import com.mapbox.maps.extension.compose.style.MapboxStyleComposable
import com.mapbox.maps.extension.compose.style.StringValue
import com.mapbox.maps.extension.compose.style.layers.generated.LineCapValue
import com.mapbox.maps.extension.compose.style.layers.generated.LineJoinValue
import com.mapbox.maps.extension.compose.style.layers.generated.LineLayer
import com.mapbox.maps.extension.compose.style.sources.GeoJSONData
import com.mapbox.maps.extension.compose.style.sources.generated.rememberGeoJsonSourceState
import com.mapbox.maps.extension.compose.style.standard.LightPresetValue
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardSatelliteStyle
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyle
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyleExperimental
import com.mapbox.maps.extension.compose.style.standard.ThemeValue
import com.mapbox.maps.extension.compose.style.standard.rememberExperimentalStandardStyleState
import com.mapbox.maps.extension.style.expressions.generated.Expression.Companion.interpolate
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.extension.style.utils.transition
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.gestures.OnMapLongClickListener
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.viewport.data.DefaultViewportTransitionOptions
import com.mapbox.maps.plugin.viewport.data.FollowPuckViewportStateOptions
import com.mapbox.maps.plugin.viewport.data.OverviewViewportStateOptions
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.presentation.util.isDark
import esi.roadside.assistance.client.main.domain.models.ServiceModel
import esi.roadside.assistance.client.main.presentation.Action
import esi.roadside.assistance.client.main.presentation.ClientState
import esi.roadside.assistance.client.main.presentation.sheet.NavigatingScreen
import esi.roadside.assistance.client.main.presentation.sheet.WorkingScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import soup.compose.material.motion.animation.materialSharedAxisZIn
import soup.compose.material.motion.animation.materialSharedAxisZOut
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, MapboxExperimental::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    currentService: ServiceModel?,
    searchState: SearchState,
    onAction: (Action) -> Unit,
    onSearchEvent: (SearchEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var point by remember { mutableStateOf<Point?>(null) }
    val state = rememberMapViewportState {
        setCameraOptions {
            zoom(2.0)
            center(Point.fromLngLat(-98.0, 39.5))
            pitch(0.0)
            bearing(0.0)
            transition {
                TransitionOptions.Builder().enablePlacementTransitions(true).duration(2000).build()
            }
        }
    }
    var progress by remember {
        mutableDoubleStateOf(0.0)
    }
    var routeLine by remember {
        mutableStateOf<LineString?>(null)
    }
    var lightPreset by remember {
        mutableStateOf(LightPresetValue.DAY)
    }
    var isStandardSatellite by remember {
        mutableStateOf(false)
    }
    var enablePlaceLabels by remember {
        mutableStateOf(true)
    }
    var enableRoadLabels by remember {
        mutableStateOf(true)
    }
    var enablePointOfInterestLabels by remember {
        mutableStateOf(true)
    }
    var enableTransitLabels by remember {
        mutableStateOf(true)
    }
    val isDark by isDark()
    var selectedLightPreset = if (isDark) LightPresetValue.NIGHT else LightPresetValue.DAY
    var selectedFont by remember {
        mutableStateOf("Inter")
    }
    var selectedTheme by remember {
        mutableStateOf(ThemeValue.DEFAULT)
    }
    var enable3dObjects by remember {
        mutableStateOf(false)
    }
    var enableRoadsAndTransit by remember {
        mutableStateOf(true)
    }
    var enablePedestrianRoads by remember {
        mutableStateOf(true)
    }
    val marker = rememberIconImage(R.drawable.baseline_location_pin_24)
    val bottomSheetState = rememberStandardBottomSheetState(SheetValue.Hidden, skipHiddenState = false, confirmValueChange = {
        it != SheetValue.Hidden
    })
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState)
    LaunchedEffect(uiState.directions) {
        routeLine = uiState.directions?.let { route ->
            withContext(Dispatchers.IO) {
                LineString.fromLngLats(route.geometry.coordinates.map {
                    Point.fromLngLat(it[0], it[1])
                }).also {
                    state.transitionToOverviewState(
                        OverviewViewportStateOptions.Builder().geometry(it)
                            .padding(
                                EdgeInsets(0.0, 100.0, 350.0, 100.0)
                            ).build()
                    )
                }
            }
        }
    }
    LaunchedEffect(uiState.clientState) {
        Log.i("MainActivity", "newState: ${uiState.clientState}")
        if (uiState.clientState in setOf(ClientState.PROVIDER_IN_WAY, ClientState.ASSISTANCE_IN_PROGRESS))
            bottomSheetState.expand()
        else
            bottomSheetState.hide()
    }
    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            when(uiState.clientState) {
                ClientState.PROVIDER_IN_WAY ->
                    NavigatingScreen(
                        uiState.providerInfo,
                        uiState.directions?.duration?.roundToInt()
                    )
                ClientState.ASSISTANCE_IN_PROGRESS ->
                    WorkingScreen(uiState.loading) {
                        onAction(Action.WorkingFinished)
                    }
                else -> {

                }
            }
        }
    ) {
        Box(Modifier.fillMaxSize().padding(it)) {
            MapboxMap(
                modifier = Modifier.fillMaxSize(),
                mapViewportState = state,
                compass = {
                    Compass(
                        modifier = Modifier.statusBarsPadding(),
                        alignment = Alignment.TopEnd
                    )
                },
                scaleBar = {
                    ScaleBar(
                        modifier = Modifier.navigationBarsPadding(),
                        alignment = Alignment.BottomStart
                    )
                },
                style = {
                    if (routeLine != null)
                        NavigationStyle(
                            routeLine = routeLine,
                            progress = progress,
                            lightPreset = selectedLightPreset
                        )
                    else if (isStandardSatellite) {
                        MapboxStandardSatelliteStyle(
                            styleTransition = remember {
                                transition {
                                    duration(1_000)
                                    enablePlacementTransitions(true)
                                }
                            }
                        ) {
                            showPlaceLabels = BooleanValue(enablePlaceLabels)
                            showRoadLabels = BooleanValue(enableRoadLabels)
                            showPointOfInterestLabels = BooleanValue(enablePointOfInterestLabels)
                            showTransitLabels = BooleanValue(enableTransitLabels)
                            lightPreset = selectedLightPreset
                            font = StringValue(selectedFont)
                            showRoadsAndTransit = BooleanValue(enableRoadsAndTransit)
                            showPedestrianRoads = BooleanValue(enablePedestrianRoads)
                        }
                    } else {
                        MapboxStandardStyleExperimental(
                            experimentalStandardStyleState = rememberExperimentalStandardStyleState {
                                interactionsState.onBuildingsClicked { clickedBuilding, _ ->
                                    clickedBuilding.setStandardBuildingsState {
                                        highlight(true)
                                    }
                                    return@onBuildingsClicked true
                                }
                                styleTransition = transition {
                                    duration(1_000)
                                    enablePlacementTransitions(true)
                                }
                                configurationsState.apply {
                                    showPlaceLabels = BooleanValue(enablePlaceLabels)
                                    showRoadLabels = BooleanValue(enableRoadLabels)
                                    showPointOfInterestLabels =
                                        BooleanValue(enablePointOfInterestLabels)
                                    showTransitLabels = BooleanValue(enableTransitLabels)
                                    lightPreset = selectedLightPreset
                                    font = StringValue(selectedFont)
                                    theme = selectedTheme
                                    show3dObjects = BooleanValue(enable3dObjects)
                                }
                            }
                        )
                    }
                },
                onMapLongClickListener = object : OnMapLongClickListener {
                    override fun onMapLongClick(newPoint: Point): Boolean {
                        point = newPoint
                        onAction(Action.SetLocation(newPoint))
                        state.easeTo(
                            CameraOptions
                                .Builder()
                                .zoom(200.0)
                                .center(newPoint)
                                .pitch(0.0)
                                .bearing(0.0)
                                .build()
                        )
                        state.transitionToOverviewState(
                            OverviewViewportStateOptions.Builder()
                                .geometry(newPoint)
                                .pitch(0.0)
                                .bearing(0.0)
                                .build()
                        )
                        return true
                    }
                },
                logo = {
                    Logo(alignment = Alignment.TopStart)
                }
            ) {
                point?.let {
                    PointAnnotation(point = it) {
                        interactionsState.onClicked {
                            onAction(Action.SetLocation(it.point))
                            state.easeTo(
                                CameraOptions
                                    .Builder()
                                    .zoom(state.cameraState?.zoom ?: 2.0)
                                    .center(it.point)
                                    .pitch(0.0)
                                    .bearing(0.0)
                                    .build()
                            )
                            true
                        }
                        iconImage = marker
                        iconAnchor = IconAnchor.BOTTOM
                        iconImageCrossFade = 1.0
                    }
                }
                currentService?.let {
                    PointAnnotation(point = it.serviceLocation.toPoint()) {
                        iconColor = Color(35, 171, 242)
                        iconImage = marker
                        iconAnchor = IconAnchor.BOTTOM
                        iconImageCrossFade = 1.0
                    }
                }
                uiState.providerLocation?.let {
                    PointAnnotation(point = it) {
//                        interactionsState.onClicked {
//                            onAction(Action.SetLocation(it.point))
//                            state.easeTo(
//                                CameraOptions
//                                    .Builder()
//                                    .zoom(state.cameraState?.zoom ?: 2.0)
//                                    .center(it.point)
//                                    .pitch(0.0)
//                                    .bearing(0.0)
//                                    .build()
//                            )
//                            true
//                        }
                        iconImage = marker
                        iconAnchor = IconAnchor.BOTTOM
                        iconImageCrossFade = 1.0
                    }
                }
                MapEffect(Unit) { mapView ->
                    followLocation(state, mapView) {
                        if (it != null) onAction(Action.SetLocation(it))
                    }
                }
                MapEffect(point) { view ->
                    point?.let {
                        view.location.updateSettings {
                            enabled = false
                        }
                    } ?: followLocation(state, view) {
                        if (it != null) onAction(Action.SetLocation(it))
                    }
                }
            }
            SearchScreen(
                searchState,
                uiState.clientState,
                uiState.time,
                onSearchEvent,
                { onAction(Action.CancelRequest) },
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                val newPoint = Point.fromLngLat(it.properties.coordinates.longitude, it.properties.coordinates.latitude)
                point = newPoint
                onAction(Action.SetLocation(newPoint))
                state.easeTo(
                    CameraOptions
                        .Builder()
                        .zoom(20.0)
                        .center(Point.fromLngLat(it.properties.coordinates.longitude, it.properties.coordinates.latitude))
                        .pitch(0.0)
                        .bearing(0.0)
                        .build()
                )
            }
            AnimatedVisibility(
                !searchState.expanded,
                enter = materialSharedAxisZIn(true),
                exit = materialSharedAxisZOut(true),
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FloatingActionButton(
                        {
                            followLocation(state) {
                                it?.let {
                                    onAction(Action.SetLocation(it))
                                    point = null
                                }
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = contentColorFor(MaterialTheme.colorScheme.tertiaryContainer)
                    ) {
                        Icon(Icons.Default.LocationOn, null)
                    }
                    AnimatedVisibility(
                        (uiState.location != null) and (uiState.clientState == ClientState.IDLE),
                        enter = materialSharedAxisZIn(true),
                        exit = materialSharedAxisZOut(true)
                    ) {
                        ExtendedFloatingActionButton(
                            onClick = {
                                onAction(Action.ShowRequestAssistance)
                            },
                            icon = { Icon(Icons.Outlined.Edit, null) },
                            text = { Text(stringResource(R.string.request_service)) },
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}



@MapboxStyleComposable
@Composable
fun NavigationStyle(
    routeLine: LineString?,
    progress: Double,
    lightPreset: LightPresetValue
) {
    val geoJsonSource = rememberGeoJsonSourceState {
        lineMetrics = BooleanValue(true)
    }
    val color = Color(72, 145, 226)
    val outline = Color(55, 112, 175)
    LaunchedEffect(routeLine) {
        routeLine?.let {
            geoJsonSource.data = GeoJSONData(it)
        }
    }
    MapboxStandardStyle(
        topSlot = {
            if (routeLine != null) {
                LineLayer(
                    sourceState = geoJsonSource
                ) {
                    lineTrimOffset = DoubleListValue(listOf(0.0, progress))
                    lineWidth = DoubleValue(
                        interpolate {
                            exponential {
                                literal(1.5)
                            }
                            zoom()
                            stop {
                                literal(10)
                                product(7.0, 1.0)
                            }
                            stop {
                                literal(14.0)
                                product(10.5, 1.0)
                            }
                            stop {
                                literal(16.5)
                                product(15.5, 1.0)
                            }
                            stop {
                                literal(19.0)
                                product(24.0, 1.0)
                            }
                            stop {
                                literal(22.0)
                                product(29.0, 1.0)
                            }
                        }
                    )
                    lineCap = LineCapValue.ROUND
                    lineJoin = LineJoinValue.ROUND
//                    lineGradient = ColorValue(
//                        interpolate {
//                            linear()
//                            lineProgress()
//                            stop {
//                                literal(0)
//                                rgba(47.0, 122.0, 198.0, 1.0)
//                            }
//                            stop {
//                                literal(1.0)
//                                rgba(47.0, 122.0, 198.0, 1.0)
//                            }
//                        }
//                    )
                    lineColor = ColorValue(outline)
                }
                LineLayer(
                    sourceState = geoJsonSource
                ) {
                    lineTrimOffset = DoubleListValue(listOf(0.0, progress))
                    lineWidth = DoubleValue(
                        interpolate {
                            exponential {
                                literal(1.5)
                            }
                            zoom()
                            stop {
                                literal(4.0)
                                product(3.0, 1.0)
                            }
                            stop {
                                literal(10.0)
                                product(4.0, 1.0)
                            }
                            stop {
                                literal(13.0)
                                product(6.0, 1.0)
                            }
                            stop {
                                literal(16.0)
                                product(10.0, 1.0)
                            }
                            stop {
                                literal(19.0)
                                product(14.0, 1.0)
                            }
                            stop {
                                literal(22.0)
                                product(18.0, 1.0)
                            }
                        }
                    )
                    lineCap = LineCapValue.ROUND
                    lineJoin = LineJoinValue.ROUND
//                    lineGradient = ColorValue(
//                        interpolate {
//                            linear()
//                            lineProgress()
//                            // blue
//                            stop { literal(0.0); rgb { literal(6); literal(1); literal(255) } }
//                            // royal blue
//                            stop { literal(0.1); rgb { literal(59); literal(118); literal(227) } }
//                            // cyan
//                            stop { literal(0.3); rgb { literal(7); literal(238); literal(251) } }
//                            // lime
//                            stop { literal(0.5); rgb { literal(0); literal(255); literal(42) } }
//                            // yellow
//                            stop { literal(0.7); rgb { literal(255); literal(252); literal(0) } }
//                            // red
//                            stop { literal(1.0); rgb { literal(255); literal(30); literal(0) } }
//                        }
//                    )
                    lineColor = ColorValue(color)
                }
            }
        }
    ) {
        this.lightPreset = lightPreset
    }
}


fun followLocation(
    state: MapViewportState,
    mapView: MapView? = null,
    listener: (Point?) -> Unit = {}
) {
    mapView?.location?.updateSettings {
        enabled = true
        locationPuck = createDefault2DPuck(withBearing = false)
        puckBearing = PuckBearing.COURSE
        puckBearingEnabled = false
    }
    state.transitionToFollowPuckState(
        followPuckViewportStateOptions = FollowPuckViewportStateOptions.Builder()
            .pitch(0.0)
            .bearing(null)
            .build(),
        defaultTransitionOptions = DefaultViewportTransitionOptions.Builder()
            .maxDurationMs(2000)
            .build()
    ) {
        if (it) listener(state.cameraState?.center)
    }
}
