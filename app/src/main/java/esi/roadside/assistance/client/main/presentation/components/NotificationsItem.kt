package esi.roadside.assistance.client.main.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.main.domain.models.NotificationModel
import java.time.LocalDateTime


@Composable
fun NotificationItem(notification: NotificationModel, onClick: () -> Unit, image : ImageVector) {

    var isWarning : Boolean = notification.title == "Warning"

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(
            containerColor = if (isWarning) MaterialTheme.colorScheme.errorContainer
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(2.dp)
        ) {
            Image(if (isWarning) Icons.Default.WarningAmber else image, null, Modifier.size(36.dp))
            Column(modifier = Modifier
                .padding(8.dp)
                .clickable { onClick() }) {

                Text(
                    notification.title,
                    fontWeight = FontWeight.Bold,
                    color = if (notification.title == stringResource(R.string.warning)) Color.Red else Color.Black
                )
                Text(notification.text, fontSize = 14.sp, color = Color.Gray)
                Text(
                    notification.createdAt.toString(),
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.End)
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
            id = "2",
            title = "Warning",
            text = "Admin sent you a warning!",
            createdAt = LocalDateTime.now()
        ),
        onClick = {},
        image = Icons.Default.WarningAmber
    )
}