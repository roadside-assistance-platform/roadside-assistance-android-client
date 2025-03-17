package esi.roadside.assistance.client.main.presentation.routes.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Title
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.presentation.components.ProfilePicturePicker
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme
import esi.roadside.assistance.client.main.presentation.Action
import esi.roadside.assistance.client.main.presentation.components.InformationCard
import esi.roadside.assistance.client.main.presentation.components.TopAppBar
import esi.roadside.assistance.client.main.presentation.models.ClientUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    state: ProfileUiState,
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(state.enableEditing) {
        if (state.enableEditing) focusRequester.requestFocus()
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(R.string.profile),
                background = R.drawable.vector_9,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                if (state.enableEditing)
                    FloatingActionButton(
                        { onAction(Action.CancelProfileEditing) },
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.error,
                    ) {
                        Icon(Icons.Default.Cancel, null)
                    }
                ExtendedFloatingActionButton(
                    onClick = {
                        onAction(
                            if (state.enableEditing) Action.ConfirmProfileEditing
                            else Action.EnableProfileEditing
                        )
                    },
                    icon = {
                        Icon(
                            imageVector =
                                if (state.enableEditing) Icons.Outlined.Check
                                else Icons.Outlined.Edit,
                            contentDescription = null
                        )
                    },
                    text = {
                        Text(
                            if (state.enableEditing) stringResource(R.string.save)
                            else stringResource(R.string.edit_profile)
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.tertiary,
                )
            }
        }
    ) {
        Column(
            modifier = modifier.verticalScroll(rememberScrollState()).fillMaxSize().imePadding().padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfilePicturePicker(
                state.editClient.photo,
                icon = Icons.Default.Person,
                enabled = state.enableEditing,
            ) {

            }
            InformationCard(
                icon = Icons.Outlined.Title,
                title = R.string.full_name,
                value = state.editClient.fullName,
                onValueChange = { onAction(Action.EditClient(
                    state.editClient.copy(
                       fullName = it
                    )
                )) },
                enabled = state.enableEditing,
                focusRequester = focusRequester
            )
            InformationCard(
                icon = Icons.Outlined.Email,
                title = R.string.email_adress,
                value = state.editClient.email,
                onValueChange = { onAction(Action.EditClient(
                    state.editClient.copy(
                        email = it
                    )
                )) },
                enabled = state.enableEditing
            )
            InformationCard(
                icon = Icons.Outlined.Phone,
                title = R.string.phone_number,
                value = state.editClient.phone,
                onValueChange = { onAction(Action.EditClient(
                    state.editClient.copy(
                        phone = it
                    )
                )) },
                enabled = state.enableEditing
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewAppTheme {
        ProfileScreen(
            state = ProfileUiState(
                client = ClientUi(
                    fullName = "John Doe",
                    email = "email@example.com",
                    phone = "0123456789"
                ),
                enableEditing = false
            ),
            {}
        )
    }
}