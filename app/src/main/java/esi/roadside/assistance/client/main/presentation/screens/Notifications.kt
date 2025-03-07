package esi.roadside.assistance.client.main.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme
import esi.roadside.assistance.client.main.domain.models.NotificationModel
import esi.roadside.assistance.client.main.presentation.components.NotificationItem
import esi.roadside.assistance.client.main.presentation.components.TopAppBar

@Composable
fun Notifications(modifier: Modifier = Modifier) {

//    var clientName by viewModel.getClientName()

    var notificationsList by remember { mutableStateOf<List<NotificationModel>>(emptyList()) }

//    notificationsList = viewModel.updateNotifications()

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        TopAppBar(title = R.string.notifications, drawableRes = R.drawable.vector_6)

        if (notificationsList.isEmpty()) {
            EmptyNotificationView()
        } else {
            LazyColumn{
                items(notificationsList){
                    NotificationItem(
                        notification = it,
                        onClick = {
                            TODO("Navigate to notification screen")
                        },
                        image = Icons.Default.Person //Change the model of notifications
                    )
                }
            }
        }
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

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewAppTheme {
        Notifications()
    }
}
