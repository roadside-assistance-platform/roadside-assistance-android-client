package esi.roadside.assistance.client.main.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun InformationCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    @StringRes title: Int,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(0.8f)
    ) {
        Row(Modifier.padding(6.dp)) {
            Icon(
                icon,
                null,
                Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(8.dp),
            )
            Column {
                Text(stringResource(title), style = MaterialTheme.typography.titleMedium)
                Text(value, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}