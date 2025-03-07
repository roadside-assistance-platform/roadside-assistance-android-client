package esi.roadside.assistance.client.main.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme
import esi.roadside.assistance.client.main.domain.models.NotificationModel
import esi.roadside.assistance.client.main.presentation.components.TopAppBar
import java.time.LocalDateTime

@Composable
fun SelectedNotificationDetails(modifier: Modifier = Modifier,
                                notificationModel: NotificationModel
                                ) {

//    var clientName by viewModel.getClientName()

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        TopAppBar(title = R.string.notifications, drawableRes = R.drawable.vector_6)

        Text(
            notificationModel.title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            notificationModel.text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth(0.87f).padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewAppTheme {
        SelectedNotificationDetails(
            notificationModel = NotificationModel(
                id = "",
                title = "Mohamed",
                text = "Dear [client_name]\n" +
                        "  \n" +
                        "  We noticed that your recent activity violates our Terms of Use\n" +
                        "\n" +
                        "  Please review our guidelines and ensure compliance in the future. Repeated violations may result in account suspension.\n" +
                        "\n" +
                        "  Thank you for your understanding.\n" +
                        "  \n" +
                        "  ",
                createdAt = LocalDateTime.now()
            )
        )
    }
}
