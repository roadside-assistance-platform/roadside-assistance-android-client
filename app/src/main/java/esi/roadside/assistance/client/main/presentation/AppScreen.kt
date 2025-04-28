package esi.roadside.assistance.client.main.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import esi.roadside.assistance.client.R
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import esi.roadside.assistance.client.core.presentation.components.Dialog
import esi.roadside.assistance.client.core.presentation.components.IconDialog
import esi.roadside.assistance.client.main.presentation.components.RatingBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    mainViewModel: MainViewModel,
    bottomSheetState: SheetState,
    modifier: Modifier = Modifier
) {
    NavigationScreen(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        snackbarHostState = snackbarHostState,
        mainViewModel = mainViewModel,
        bottomSheetState = bottomSheetState,
    )
    val uiState by mainViewModel.homeUiState.collectAsState()
    var rating by remember { mutableDoubleStateOf(0.0) }
    IconDialog(
        visible = uiState.clientState == ClientState.ASSISTANCE_FAILED,
        onDismissRequest = {
            mainViewModel.onAction(Action.CompleteRequest(null))
        },
        title = stringResource(R.string.assistance_failed),
        text = stringResource(R.string.assistance_failed_description),
        icon = Icons.Default.Error,
        okListener = {
            mainViewModel.onAction(Action.SubmitRequest)
        },
        cancelListener = {
            mainViewModel.onAction(Action.CancelRequest)
        },
        cancelText = stringResource(R.string.cancel),
        okText = stringResource(R.string.retry),
    )
    Dialog(
        visible = uiState.clientState == ClientState.ASSISTANCE_COMPLETED,
        onDismissRequest = {
            mainViewModel.onAction(Action.CompleteRequest(null))
        },
        title = stringResource(R.string.assistance_completed),
        okListener = {
            mainViewModel.onAction(Action.CompleteRequest(rating))
        },
        cancelListener = {
            mainViewModel.onAction(Action.CompleteRequest(null))
        },
        cancelText = stringResource(R.string.close),
    ) {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                stringResource(R.string.leave_review),
                Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
            RatingBar(rating, { rating = it })
        }
    }
}