package esi.roadside.assistance.client.main.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Notifications(modifier: Modifier = Modifier,
                  clientName : String? = null,
                  providerName : String? = null,
                  image: ImageVector
                  ) {
    var notifications by remember { mutableStateOf(sampleNotifications) }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        NotificationAppBar()

        if (notifications.isEmpty()) {
            EmptyNotificationView()
        } else {
            NotificationList(notifications, image) { selectedNotification ->
                //navigation

            }
        }
    }
}

@Composable
fun WarningNotification(clientName: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(stringResource(R.string.warning), style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.error
            )
        Text(stringResource(R.string.dear) + clientName +"\n"
                + stringResource(R.string.warning_des),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }

}

@Composable
fun AcceptedNotification(providerName: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(stringResource(R.string.accepted), style = MaterialTheme.typography.displaySmall
        )
        Text(stringResource(R.string.accepted_by_1) + providerName ,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Text(stringResource(R.string.accepted_by_2) + providerName ,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun NotificationAppBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f),
        contentAlignment = Alignment.Center
    ) {
        Image(painterResource(R.drawable.vector_6), null, Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
            )
        Text(stringResource(R.string.notifications), style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.background
            )
    }
}

@Preview
@Composable
fun EmptyNotificationView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painterResource(R.drawable.shrug_bro_1), null)
        Text(stringResource(R.string.no_notifications), fontSize = 16.sp,)
    }
}

@Composable
fun NotificationList(notifications: List<Notification>,
                     image : ImageVector,
                     onNotificationClick: (Notification) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        notifications.forEach { notification ->
            NotificationItem(notification,image = image, onClick = {onNotificationClick(notification)})
        }
    }
}

@Composable
fun NotificationItem(notification: Notification, onClick: () -> Unit, image : ImageVector) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isWarning) MaterialTheme.colorScheme.errorContainer
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(2.dp)
        ) {
            Image(if (notification.isWarning) Icons.Default.WarningAmber else image, null, Modifier.size(36.dp))
            Column(modifier = Modifier
                .padding(8.dp)
                .clickable { onClick() }) {

                Text(
                    notification.title,
                    fontWeight = FontWeight.Bold,
                    color = if (notification.isWarning) Color.Red else Color.Black
                )
                Text(notification.message, fontSize = 14.sp, color = Color.Gray)
                Text(
                    notification.time,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }

    }
}

// Sample data
data class Notification(val title: String, val message: String, val time: String, val isWarning: Boolean = false)

val sampleNotifications = listOf<Notification>(
    Notification("Mohammed", "accepted your request!", "9:41 AM"),
    Notification("Warning", "Admin sent you a warning!", "9:41 AM", isWarning = true)
)

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewAppTheme {
        Notifications(
            image = Icons.Default.Person
        )
    }
}
