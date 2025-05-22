package esi.roadside.assistance.client.main.presentation.components

import android.content.ClipData
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.presentation.theme.AppTheme
import esi.roadside.assistance.client.main.domain.Categories
import esi.roadside.assistance.client.main.domain.models.ProviderInfo
import esi.roadside.assistance.client.main.domain.models.ServiceModel
import esi.roadside.assistance.client.main.domain.repository.ServiceState
import esi.roadside.assistance.client.main.presentation.ClientState
import esi.roadside.assistance.client.main.util.formatPrice
import esi.roadside.assistance.client.main.util.formatShortTime
import esi.roadside.assistance.client.main.util.plus
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinishDialog(
    loading: Boolean,
    serviceState: ServiceState,
    onDismiss: (Double?) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(true) { it != SheetValue.Hidden }
    if (serviceState.clientState == ClientState.ASSISTANCE_COMPLETED) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss(null) },
            sheetState = sheetState,
            dragHandle = null
        ) {
            Content(loading, serviceState, onDismiss)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(
    loading: Boolean,
    serviceState: ServiceState,
    onDismiss: (Double?) -> Unit,
    modifier: Modifier = Modifier
) {
    var rating by remember { mutableDoubleStateOf(0.0) }
    val serviceData = listOf(
        ItemData(
            icon = Icons.Default.Category,
            label = stringResource(R.string.category),
            text = serviceState.serviceModel?.category?.text?.let { stringResource(it) } ?: ""
        ),
        ItemData(
            icon = Icons.Default.LocationOn,
            label = stringResource(R.string.location),
            text = "Press and hold to copy",
            value = serviceState.serviceModel?.serviceLocation?.toString() ?: ""
        ),
        ItemData(
            icon = Icons.Default.Timer,
            label = stringResource(R.string.request_time),
            text = serviceState.serviceModel?.createdAt?.toLocalDateTime()?.formatShortTime() ?: ""
        ),
    )
    val providerData = listOf(
        ItemData(
            icon = Icons.Default.Person,
            label = stringResource(R.string.full_name),
            text = serviceState.providerInfo?.fullName ?: ""
        ),
        ItemData(
            icon = Icons.Default.Phone,
            label = stringResource(R.string.phone_number),
            text = serviceState.providerInfo?.phone ?: ""
        ),
        ItemData(
            icon = Icons.Default.Mail,
            label = stringResource(R.string.email_adress),
            text = serviceState.providerInfo?.email ?: ""
        )
    )
    Scaffold(
        modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(id = R.string.finish_service))
                }
            )
        },
        bottomBar = {
            Surface(Modifier.fillMaxWidth()) {
                AnimatedContent(loading) {
                    if (it)
                        LinearProgressIndicator(Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 30.dp))
                    else
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton({ onDismiss(null) }, Modifier.weight(1f)) {
                                Text(stringResource(id = R.string.close))
                            }
                            Button({ onDismiss(rating) }, Modifier.weight(1f)) {
                                Text(stringResource(id = R.string.ok))
                            }
                        }
                }
            }
        }
    ) {
        LazyColumn(
            contentPadding = it.plus(PaddingValues(horizontal = 16.dp, vertical = 24.dp)),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Group(stringResource(R.string.service), items = serviceData)
            }
            item {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.price),
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        text = serviceState.price.formatPrice(),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }
            item {
                HorizontalDivider()
            }
            item {
                Group(stringResource(R.string.provider), items = providerData)
            }
            item {
                Text(
                    stringResource(R.string.assistance_completed_description),
                    textAlign = TextAlign.Center,
                )
            }
            item {
                RatingBar(
                    rating,
                    { rating = it },
                    Modifier.fillMaxWidth(.75f),
                    starsColor = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}


@Composable
fun Heading(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text.uppercase(),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

data class ItemData(
    val icon: ImageVector,
    val label: String,
    val text: String,
    val value: String = text
)

@Composable
fun Item(
    data: ItemData,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()
    Row(
        modifier = modifier
            .combinedClickable(onLongClick = {
                scope.launch {
                    clipboard.setClipEntry(ClipEntry(ClipData.newPlainText("Location", data.value)))
                }
            }) {
                Toast.makeText(context, "${data.label}: ${data.value}", Toast.LENGTH_SHORT).show()
            }
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            data.icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = data.label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = data.text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun Group(
    heading: String,
    items: List<ItemData>,
    modifier: Modifier = Modifier,
    separator: Boolean = true,
) {
    Column(
        modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Heading(heading)
        items.forEachIndexed { index, item ->
            Item(item)
        }
        if (separator) {
            HorizontalDivider(Modifier.fillMaxWidth())
        }
    }
}

@Preview
@Composable
private fun FinishDialogPreview() {
    AppTheme {
        Content(
            false,
            ServiceState(
                clientState = ClientState.ASSISTANCE_COMPLETED,
                serviceModel = ServiceModel(),
                providerInfo = ProviderInfo(
                    id = "123",
                    fullName = "Younes Bouhouche",
                    phone = "+21369054400",
                    photo = "",
                    email = "younesbouh05@gmail.com",
                    categories = setOf(Categories.TOWING)
                ),
                price = 12000
            ),
            {}
        )
    }
}