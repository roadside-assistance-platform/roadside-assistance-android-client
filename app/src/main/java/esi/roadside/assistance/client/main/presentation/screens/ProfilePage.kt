package esi.roadside.assistance.client.main.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme

@Composable
fun ProfilePage(modifier: Modifier = Modifier) {

//    val clientName by viewModel.....

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
        ProfileAppBar("client Name", Icons.Default.Person)
        Text(
            "Client name", Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
        Card(
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Row(Modifier.padding(6.dp)) {
                Icon(
                    Icons.Outlined.Email,
                    null,
                    Modifier.background(MaterialTheme.colorScheme.primaryContainer).padding(8.dp),
                )
                Column {
                    Text(stringResource(R.string.email_adress), style = MaterialTheme.typography.titleMedium)
                    Text("jsdlkfjlkjsdf@lkjfsdj.kjsf", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
        Card(
            modifier = Modifier.fillMaxWidth(0.8f).padding(top = 20.dp)
        ) {
            Row(Modifier.padding(6.dp)) {
                Icon(
                    Icons.Outlined.Phone,
                    null,
                    Modifier.background(MaterialTheme.colorScheme.primaryContainer).padding(8.dp),
                )
                Column {
                    Text(stringResource(R.string.email_adress), style = MaterialTheme.typography.titleMedium)
                    Text("0156156156", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

@Composable
fun ProfileAppBar(clientName: String, image: ImageVector) {
    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.35f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painterResource(R.drawable.vector_9), null, Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Text(
                stringResource(R.string.profile), style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.background
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.35f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(image, null, Modifier.size(106.dp))
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewAppTheme {
        ProfilePage()
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfilePage() {
    var name by remember { mutableStateOf(TextFieldValue("Client Name")) }
    var email by remember { mutableStateOf(TextFieldValue("example@email.com")) }
    var phone by remember { mutableStateOf(TextFieldValue("0123456789")) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProfileAppBar("Edit Profile", Icons.Default.Person)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(R.string.client_name)) },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email_adress)) },
            leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text(stringResource(R.string.phone_number)) },
            leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Button(
            onClick = { /* Save changes */ },
            modifier = Modifier.fillMaxWidth(0.8f),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = stringResource(R.string.save_changes))
        }
    }
}