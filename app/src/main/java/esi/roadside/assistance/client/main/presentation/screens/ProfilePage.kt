package esi.roadside.assistance.client.main.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme
import esi.roadside.assistance.client.main.presentation.components.InformationCard
import esi.roadside.assistance.client.main.presentation.components.TopAppBar

@Composable
fun ProfilePage(modifier: Modifier = Modifier) {

//    val clientName by viewModel.getClientName()
//    val image by viewModel.getImageOfClient()
//    val email by viewModel.getEmail()

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
        TopAppBar(title = R.string.profile, drawableRes = R.drawable.vector_9, height = 0.35f, image = Icons.Default.Person)
        Text(
            "Client name", Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
        InformationCard(
            icon = Icons.Outlined.Email,
            title = R.string.email_adress,
            value = "jsdlkfjlkjsdf@lkjfsdj.kjsf"
        )
        InformationCard(
            icon = Icons.Outlined.Phone,
            title = R.string.phone_number,
            value = "0156156156"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewAppTheme {
        ProfilePage()
    }
}