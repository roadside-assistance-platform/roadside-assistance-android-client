package esi.roadside.assistance.client.main.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.presentation.components.IconDialog
import esi.roadside.assistance.client.main.presentation.components.FinishDialog
import esi.roadside.assistance.client.main.presentation.routes.home.request.AssistanceAction
import esi.roadside.assistance.client.main.presentation.routes.home.request.AssistanceViewModel
import esi.roadside.assistance.client.main.presentation.routes.home.request.RequestAssistance
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    requestSheetState: SheetState,
    modifier: Modifier = Modifier
) {
    val mainViewModel: MainViewModel = koinViewModel()
    val assistanceViewModel: AssistanceViewModel = koinViewModel()
    val assistanceState by assistanceViewModel.state.collectAsState()
    val uiState by mainViewModel.homeUiState.collectAsState()
    val service by mainViewModel.currentService.collectAsState()
    NavigationScreen(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        snackbarHostState = snackbarHostState,
        mainViewModel = mainViewModel,
        onLocationChange = { location ->
            mainViewModel.onAction(Action.SetLocation(location))
            assistanceViewModel.onAction(AssistanceAction.SetLocation(location))
        },
        onRequest = {
            assistanceViewModel.onAction(AssistanceAction.ShowSheet)
        }
    )
    IconDialog(
        visible = service.clientState == ClientState.ASSISTANCE_FAILED,
        onDismissRequest = {
            mainViewModel.onAction(Action.CancelRequest)
        },
        title = stringResource(R.string.assistance_failed),
        text = stringResource(R.string.assistance_failed_description),
        icon = Icons.Default.Error,
        okListener = {
            //mainViewModel.onAction(Action.SubmitRequest)
        },
        cancelListener = {
            mainViewModel.onAction(Action.CancelRequest)
        },
        cancelText = stringResource(R.string.cancel),
        okText = stringResource(R.string.retry),
    )
    FinishDialog(
        loading = uiState.loading,
        serviceState = service,
        onDismiss = {
            mainViewModel.onAction(Action.CompleteRequest(it))
        },
    )
    RequestAssistance(requestSheetState, assistanceState, assistanceViewModel::onAction)
}