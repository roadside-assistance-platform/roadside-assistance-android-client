package esi.roadside.assistance.client.main.presentation.sheet

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.main.domain.models.ProviderInfo
import esi.roadside.assistance.client.main.presentation.components.MyImage

@Composable
fun NavigatingScreen(
    provider: ProviderInfo?,
    eta: Int?,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(provider) {
        Log.d("NavigatingScreen", "Provider: $provider")
    }
    LazyColumn(
        modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Text(
                stringResource(R.string.provider_coming),
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.titleLarge
            )
        }
        provider?.let {
            item {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MyImage(
                        model = provider.photo,
                        icon = Icons.Default.Person,
                        modifier = Modifier.size(60.dp),
                        shape = CircleShape,
                    )
                    Column(
                        Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            provider.fullName,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            provider.phone,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            item {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FilledTonalIconButton({}) {
                        Icon(Icons.Default.Phone, null)
                    }
                    FilledTonalIconButton({}) {
                        Icon(Icons.AutoMirrored.Filled.Message, null)
                    }
                    FilledTonalIconButton({}) {
                        Icon(Icons.Default.Email, null)
                    }
                }
            }
        }
        item {
            Text(
                eta?.let { "Estimated time of arrival: $eta minutes" }
                    ?: stringResource(R.string.eta_not_available),
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}