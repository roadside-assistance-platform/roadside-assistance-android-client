package esi.roadside.assistance.client.main.presentation.routes.notifications

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme
import esi.roadside.assistance.client.main.domain.models.NotificationModel
import esi.roadside.assistance.client.main.presentation.components.NotificationItem
import esi.roadside.assistance.client.main.presentation.components.TopAppBar
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    list: List<NotificationModel> = emptyList(),
    modifier: Modifier = Modifier,
    onClick: (NotificationModel) -> Unit = {}
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(R.string.notifications),
                background = R.drawable.vector_6,
                scrollBehavior = scrollBehavior
            )
        },
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        AnimatedContent(list.isEmpty(), Modifier.fillMaxSize().padding(it), label = "") {
            if (it)
                EmptyNotificationView(Modifier.fillMaxSize())
            else
                LazyColumn(
                    Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(list, { it.id }){
                        NotificationItem(
                            notification = it,
                            onClick = {
                                onClick(it)
                            },
                        )
                    }
                }

        }
    }
}

@Composable
fun EmptyNotificationView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        Image(painterResource(R.drawable.shrug_bro_1), null, Modifier.fillMaxWidth())
        Text(
            stringResource(R.string.no_notifications),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewAppTheme {
        NotificationsScreen(
            list = List(5) {
                NotificationModel(
                    id = it.toString(),
                    title = "Notification $it",
                    text = "Description $it",
                    createdAt = LocalDateTime.of(2021, 10, 10, 10, 10),
                    isWarning = it % 2 == 0,
                    image = "https://picsum.photos/200/300"
                )
            }
        )
    }
}
