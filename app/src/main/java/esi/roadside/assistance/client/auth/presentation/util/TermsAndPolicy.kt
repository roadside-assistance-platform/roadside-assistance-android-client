package esi.roadside.assistance.client.auth.presentation.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R

@Composable
fun TermsAndPolicy(modifier: Modifier = Modifier) {
    Row(modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        TextButton({},
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.tertiary
            )) {
            Text(
                stringResource(R.string.terms_of_service),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        VerticalDivider(Modifier.fillMaxHeight(.5f))
        TextButton({},
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.tertiary
            )) {
            Text(
                stringResource(R.string.privacy_policy),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}