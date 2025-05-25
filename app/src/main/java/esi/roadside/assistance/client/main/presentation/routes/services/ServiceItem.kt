package esi.roadside.assistance.client.main.presentation.routes.services

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.main.domain.models.ServiceModel

@Composable
fun ServiceItem(
    service: ServiceModel,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(PaddingValues(16.dp))) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (service.done) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = service.category.icon),
                            contentDescription = null,
                            tint = if (service.done) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onTertiary,
                            modifier = Modifier.padding(4.dp).size(20.dp)
                        )
                    }
                }
                Column(
                    Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(id = service.category.text),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                Text(
                    text = "${service.price} DA",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            if (service.serviceLocationString.isNotBlank()) {
                Text(
                    text = service.serviceLocationString,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
