package esi.roadside.assistance.client.main.presentation.routes.profile

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.presentation.components.ProfilePicturePicker
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme
import esi.roadside.assistance.client.main.presentation.components.DefaultBackNavButton
import esi.roadside.assistance.client.main.presentation.components.InformationCard
import esi.roadside.assistance.client.main.presentation.components.TopAppBar
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    val viewModel: ProfileViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val focusRequester = remember { FocusRequester() }
    var image by remember { mutableStateOf<Any?>(null) }
    LaunchedEffect(state.enableEditing) {
        if (state.enableEditing) focusRequester.requestFocus()
    }
    LaunchedEffect(state.editClient.photo) {
        if (state.enableEditing) image = state.editClient.photo
    }
    LaunchedEffect(state.photo) {
        if (!state.enableEditing) image = state.photo
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(R.string.profile),
                background = R.drawable.vector_9,
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    AnimatedVisibility(!state.enableEditing) {
                        DefaultBackNavButton()
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedContent(state.loading) {
                if (it)
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                else {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End)
                    ) {
                        if (state.enableEditing)
                            FloatingActionButton(
                                { viewModel.onAction(ProfileAction.CancelProfileEditing) },
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.error,
                            ) {
                                Icon(Icons.Default.Cancel, null)
                            }
                        ExtendedFloatingActionButton(
                            onClick = {
                                viewModel.onAction(
                                    if (state.enableEditing) ProfileAction.ConfirmProfileEditing
                                    else ProfileAction.EnableProfileEditing
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
            }
        }
    ) {
        Column(
            modifier = modifier.verticalScroll(rememberScrollState())
                .fillMaxSize()
                .imePadding()
                .padding(it)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ProfilePicturePicker(
                image = image,
                icon = Icons.Default.Person,
                enabled = state.enableEditing,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                viewModel.onAction(ProfileAction.EditClient(
                    state.editClient.copy(
                        photo = it
                    )
                ))
            }
            InformationCard(
                icon = Icons.Outlined.Title,
                title = R.string.full_name,
                text = state.client.fullName,
                value = state.editClient.fullName,
                onValueChange = { viewModel.onAction(ProfileAction.EditClient(
                    state.editClient.copy(
                       fullName = it
                    )
                )) },
                enabled = state.enableEditing,
                focusRequester = focusRequester,
                error = state.fullNameError
            )
            InformationCard(
                icon = Icons.Outlined.Email,
                title = R.string.email_adress,
                text = state.client.email,
                value = state.editClient.email,
                onValueChange = { viewModel.onAction(ProfileAction.EditClient(
                    state.editClient.copy(
                        email = it
                    )
                )) },
                enabled = state.enableEditing,
                error = state.emailError
            )
            InformationCard(
                icon = Icons.Outlined.Phone,
                title = R.string.phone_number,
                text = state.client.phone,
                value = state.editClient.phone,
                onValueChange = { viewModel.onAction(ProfileAction.EditClient(
                    state.editClient.copy(
                        phone = it
                    )
                )) },
                enabled = state.enableEditing,
                error = state.phoneError
            )
        }
    }
    BackHandler(enabled = state.enableEditing) {
        viewModel.onAction(ProfileAction.CancelProfileEditing)
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewAppTheme {
//        ProfileScreen(
//            state = ProfileUiState(
//                client = ClientUi(
//                    fullName = "John Doe",
//                    email = "email@example.com",
//                    phone = "0123456789"
//                ),
//                enableEditing = false
//            ),
//            {}
//        )
    }
}