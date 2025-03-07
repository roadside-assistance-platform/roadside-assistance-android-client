package esi.roadside.assistance.client.main.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme
import esi.roadside.assistance.client.main.presentation.components.TopAppBar

@Composable
fun EditProfilePage() {

//        val clientName by viewModel.getClientName()
//        val image by viewModel.getImageOfClient()
//        val email by viewModel.getEmail()

    var name by remember { mutableStateOf(TextFieldValue("Client Name")) }
    var email by remember { mutableStateOf(TextFieldValue("example@email.com")) }
    var phone by remember { mutableStateOf(TextFieldValue("0123456789")) }

    var isClientNameOnEditing by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TopAppBar(title = R.string.profile, drawableRes = R.drawable.vector_9, height = 0.35f)

        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicTextField(
                value = name,
                onValueChange = { if(isClientNameOnEditing) name = it },
            )
            IconButton(
                {
                    isClientNameOnEditing = !isClientNameOnEditing
                }
            ) {
                Icon(if (isClientNameOnEditing) Icons.Default.Check else Icons.Default.Edit, null)
            }
        }

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

    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewAppTheme {
        EditProfilePage()
    }
}