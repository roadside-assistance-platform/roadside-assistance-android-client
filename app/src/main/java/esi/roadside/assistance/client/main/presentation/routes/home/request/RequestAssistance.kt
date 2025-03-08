package esi.roadside.assistance.client.main.presentation.routes.home.request

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme
import esi.roadside.assistance.client.main.domain.Categories
import esi.roadside.assistance.client.main.presentation.Action
import esi.roadside.assistance.client.main.presentation.components.TopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestAssistance(
    state: RequestAssistanceState,
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(R.string.request_assistance),
                background = R.drawable.union,
                text = stringResource(R.string.request_assistance_des)
            )
        },
        bottomBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Button(
                    onClick = { onAction(Action.SubmitRequest) },
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(text = stringResource(R.string.submit_req))
                }
            }
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it).padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.selecte_the_field),
                style = MaterialTheme.typography.titleSmall
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
            ) {
                items(Categories.entries, { it.name }) {
                    InputChip(
                        modifier = modifier.wrapContentSize(),
                        selected = state.category == it,
                        onClick = { onAction(Action.SelectCategory(it)) },
                        label = {
                            Text(
                                text = stringResource(it.text),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = it.icon),
                                contentDescription = null,
                                modifier = Modifier.size(InputChipDefaults.IconSize)
                            )
                        },
                        colors = InputChipDefaults.inputChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.tertiary,
                            selectedLeadingIconColor = MaterialTheme.colorScheme.tertiary,
                        )
                    )
                }
            }
            Text(
                text = stringResource(R.string.des_issue),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            BasicTextField(
                value = state.description,
                onValueChange = { onAction(Action.SetDescription(it)) },
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surfaceContainer),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    Box(Modifier.fillMaxSize().padding(16.dp)) {
                        innerTextField()
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun RequestAssistancePreview() {
    PreviewAppTheme {
        RequestAssistance(
            state = RequestAssistanceState(),
            onAction = {},
        )
    }
}