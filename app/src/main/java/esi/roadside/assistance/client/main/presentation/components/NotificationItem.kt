package esi.roadside.assistance.client.main.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import esi.roadside.assistance.client.main.domain.models.NotificationModel
import esi.roadside.assistance.client.main.util.toStringDate
import java.time.LocalDateTime


@Composable
fun NotificationItem(
    notification: NotificationModel,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor =
                if (notification.isWarning) MaterialTheme.colorScheme.errorContainer
                else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier.size(60.dp)
                    .clip(CircleShape)
                    .background(
                        if (notification.isWarning) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.surfaceContainer
                    ),
                contentAlignment = Alignment.Center,
                ) {
                Icon(
                    if (notification.isWarning) Icons.Default.WarningAmber
                    else Icons.Default.Person,
                    null,
                    Modifier.size(30.dp),
                    tint =
                        if (notification.isWarning) MaterialTheme.colorScheme.errorContainer
                        else MaterialTheme.colorScheme.onSurfaceVariant
                )
                AsyncImage(
                    model = notification.image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable(onClick = onClick),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    notification.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color =
                        if (notification.isWarning) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    notification.text,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    notification.createdAt.toStringDate(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

    }
}

@Preview
@Composable
private fun Preview() {
    NotificationItem(
        notification = NotificationModel(
            id = "1",
            title = "Notification Title",
            text = "Notification Text",
            createdAt = LocalDateTime.now(),
            isWarning = false,
            image = "https://picsum.photos/200/300"
        ),
        onClick = {}
    )
}

@Preview
@Composable
private fun WarningPreview() {
    NotificationItem(
        notification = NotificationModel(
            id = "1",
            title = "Notification Title",
            text = "Notification Text",
            createdAt = LocalDateTime.now(),
            isWarning = true,
            image = "https://picsum.photos/200/300"
        ),
        onClick = {}
    )
}