package esi.roadside.assistance.client.main.presentation.routes.services

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme
import esi.roadside.assistance.client.main.domain.Categories
import esi.roadside.assistance.client.main.domain.models.CommentModel
import esi.roadside.assistance.client.main.domain.models.LocationModel
import esi.roadside.assistance.client.main.domain.models.ServiceModel
import esi.roadside.assistance.client.main.presentation.Action
import esi.roadside.assistance.client.main.presentation.components.TopAppBar
import esi.roadside.assistance.client.main.util.plus
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetails(
    service: ServiceModel,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(R.string.service_details),
                background = R.drawable.union,
                scrollBehavior = scrollBehavior
            )
        },
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = paddingValues + PaddingValues(16.dp)
        ) {
            item {
                StatusCard(service)
            }

            item {
                ServiceInfoCard(service)
            }

            item {
                LocationCard(service)
            }

            item {
                DescriptionCard(service)
            }

            if (service.comments.isNotEmpty()) {
                item {
                    CommentsSection(service.comments)
                }
            }
        }
    }
}

@Composable
private fun StatusCard(service: ServiceModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (service.done) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = if (service.done) Icons.Default.CheckCircle else Icons.Default.Pending,
                    contentDescription = null,
                    tint = if (service.done) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = stringResource(if (service.done) R.string.completed else R.string.in_progress),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (service.done) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
                )
            }

            if (service.serviceRating > 0) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${service.serviceRating.toInt()}/5",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ServiceInfoCard(service: ServiceModel) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.service_information),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoRow(
                    icon = ImageVector.vectorResource(service.category.icon),
                    label = stringResource(R.string.category),
                    value = stringResource(service.category.text)
                )
            }

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoRow(
                    icon = Icons.Default.AttachMoney,
                    label = "Price",
                    value = "${service.price}.00 DA"
                )
            }

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoRow(
                    icon = Icons.Default.Schedule,
                    label = "Created",
                    value = service.createdAt.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                )
            }
        }
    }
}

@Composable
private fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun LocationCard(service: ServiceModel) {
    val state = rememberMapViewportState {
        setCameraOptions {
            center(service.serviceLocation.toPoint())
            zoom(12.0)
            pitch(0.0)
            bearing(0.0)
        }
    }
    val marker = rememberIconImage(R.drawable.baseline_location_pin_24)
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Location",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = service.serviceLocationString.ifEmpty {
                    "Lat: ${service.serviceLocation.latitude}, Long: ${service.serviceLocation.longitude}"
                },
                style = MaterialTheme.typography.bodyLarge
            )

            // Map placeholder - in a real app, you would show a map here
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(top = 8.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    MapboxMap(
                        modifier = Modifier.fillMaxSize(),
                        mapViewportState = state,
                        scaleBar = {
                            ScaleBar(
                                alignment = Alignment.BottomEnd
                            )
                        }
                    ) {
                        PointAnnotation(point = service.serviceLocation.toPoint()) {
                            iconImage = marker
                            iconAnchor = IconAnchor.BOTTOM
                            iconImageCrossFade = 1.0
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DescriptionCard(service: ServiceModel) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Description",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = service.description ?: "No description provided",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun CommentsSection(comments: List<CommentModel>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Comment,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Comments (${comments.size})",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            comments.forEach { comment ->
                CommentItem(comment)
            }
        }
    }
}

@Composable
private fun CommentItem(comment: CommentModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (comment.providerId == comment.clientId) "Provider" else "Client",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = comment.createdAt.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = comment.text,
            style = MaterialTheme.typography.bodyMedium
        )

        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewAppTheme {
        ServiceDetails(
            service =
               ServiceModel(
                   id = "service123",
                   clientId = "client456",
                   providerId = "provider789",
                   price = 1500,
                   description = "Flat tire replacement on highway.",
                   serviceRating = 4.5f,
                   serviceLocation = LocationModel(36.7525, 3.0420),
                   serviceLocationString = "Algiers, Algeria",
                   done = true,
                   category = Categories.FLAT_TIRE,
                   createdAt = ZonedDateTime.now().minusDays(2),
                   updatedAt = ZonedDateTime.now(),
                   comments = listOf(
                       CommentModel(
                            id = "comment1",
                            clientId = "user123",
                            providerId = "user123",
                            text = "Great service!",
                            createdAt = ZonedDateTime.now().minusDays(1),
                            updatedAt = ZonedDateTime.now()
                          ),
                       ),
               ),

            modifier = Modifier.fillMaxSize()
        )
    }
}
