package esi.roadside.assistance.client.main.presentation.routes.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotation
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.gestures.OnMapLongClickListener
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.viewport.data.FollowPuckViewportStateOptions
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.main.presentation.Action
import soup.compose.material.motion.animation.materialSharedAxisZIn
import soup.compose.material.motion.animation.materialSharedAxisZOut

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    onAction: (Action) -> Unit
) {
    var point by remember { mutableStateOf<Point?>(null) }
    val state = rememberMapViewportState {
        setCameraOptions {
            zoom(2.0)
            center(Point.fromLngLat(-98.0, 39.5))
            pitch(0.0)
            bearing(0.0)
        }
    }
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
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
                    uiState.location != null,
                    enter = materialSharedAxisZIn(true),
                    exit = materialSharedAxisZOut(true)
                ) {
                    ExtendedFloatingActionButton(
                        onClick = {
                            onAction(Action.RequestService)
                        },
                        icon = { Icon(Icons.Outlined.Edit, null) },
                        text = { Text(stringResource(R.string.request_service)) }
                    )
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
                onMapLongClickListener = object : OnMapLongClickListener {
                    override fun onMapLongClick(newPoint: Point): Boolean {
                        point = newPoint
                        onAction(Action.SetLocation(newPoint))
                        state.easeTo(
                            CameraOptions
                                .Builder()
                                .zoom(state.cameraState?.zoom ?: 2.0)
                                .center(newPoint)
                                .pitch(0.0)
                                .bearing(0.0)
                                .build()
                        )
                        return true
                    }
                },
                logo = {}
            ) {
                point?.let {
                    CircleAnnotation(point = it) {
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
                        circleRadius = 10.0
                        circleColor = Color.Red
                        circleStrokeWidth = 5.0
                        circleStrokeColor = Color.White
                    }
                }
                MapEffect(Unit) { mapView ->
                    followLocation(state, mapView) {
                        if (it != null) onAction(Action.SetLocation(it))
                    }
                }
            }
            FilledIconButton(
                { onAction(Action.OpenNotifications) },
                Modifier.align(Alignment.TopEnd).statusBarsPadding().offset((-24).dp)
            ) {
                Icon(Icons.Default.Notifications, null)
            }
        }
    }
}

fun followLocation(
    state: MapViewportState,
    mapView: MapView? = null,
    listener: (Point?) -> Unit = {}
) {
    mapView?.location?.updateSettings {
        locationPuck = createDefault2DPuck(withBearing = false)
        enabled = true
        puckBearing = PuckBearing.COURSE
        puckBearingEnabled = false
    }
    state.transitionToFollowPuckState(
        followPuckViewportStateOptions = FollowPuckViewportStateOptions.Builder()
            .pitch(0.0)
            .bearing(null)
            .build()
    ) {
        if (it) listener(state.cameraState?.center)
    }
}
