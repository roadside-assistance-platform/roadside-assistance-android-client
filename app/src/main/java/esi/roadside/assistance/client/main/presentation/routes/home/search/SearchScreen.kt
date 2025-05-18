package esi.roadside.assistance.client.main.presentation.routes.home.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.inputFieldColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.util.intUpDownTransSpec
import esi.roadside.assistance.client.main.domain.models.geocoding.Feature
import esi.roadside.assistance.client.main.presentation.ClientState
import esi.roadside.assistance.client.main.util.formatTime
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut
import soup.compose.material.motion.animation.materialSharedAxisZ

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    state: SearchState,
    clientState: ClientState,
    time: Long,
    onSearchEvent: (SearchAction) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    onItemClick: (Feature) -> Unit,
) {
    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = state.query,
                onQueryChange = { onSearchEvent(SearchAction.UpdateQuery(it)) },
                onSearch = { onSearchEvent(SearchAction.UpdateQuery(it)) },
                expanded = state.expanded,
                placeholder = {
                    AnimatedContent(clientState) {
                        Text(
                            if (it == ClientState.ASSISTANCE_REQUESTED)
                                stringResource(R.string.looking_for_providers)
                            else
                                stringResource(R.string.search)
                        )
                    }
                },
                enabled = clientState != ClientState.ASSISTANCE_REQUESTED,
                colors = inputFieldColors(
                    disabledPlaceholderColor =
                        if (clientState == ClientState.ASSISTANCE_REQUESTED) MaterialTheme.colorScheme.onSurface
                        else Color.Unspecified,
                    disabledTextColor =
                        if (clientState == ClientState.ASSISTANCE_REQUESTED) MaterialTheme.colorScheme.onSurface
                        else Color.Unspecified,
                    disabledTrailingIconColor =
                        if (clientState == ClientState.ASSISTANCE_REQUESTED) MaterialTheme.colorScheme.onSurface
                        else Color.Unspecified,
                ),
                leadingIcon = {
                    AnimatedContent(
                        clientState,
                        transitionSpec = {
                            materialSharedAxisZ(true)
                        }
                    ) {
                        if (it == ClientState.ASSISTANCE_REQUESTED)
                            CircularProgressIndicator(
                                Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        else {
                            if (state.expanded) {
                                IconButton(onClick = { onSearchEvent(SearchAction.Collapse) }) {
                                    Icon(Icons.AutoMirrored.Default.ArrowBack, null)
                                }
                            } else {
                                IconButton(onClick = { onSearchEvent(SearchAction.Expand) }) {
                                    Icon(Icons.Default.Search, null)
                                }
                            }
                        }
                    }
                },
                trailingIcon = {
                    AnimatedContent(clientState) {
                        if (it == ClientState.ASSISTANCE_REQUESTED)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row {
                                    time.formatTime().forEach {
                                        if (it.isDigit())
                                            AnimatedContent(
                                                it.digitToInt(),
                                                transitionSpec = intUpDownTransSpec
                                            ) { digit ->
                                                Text("$digit")
                                            }
                                        else
                                            Text("$it")
                                    }
                                }
                                IconButton(onCancel) {
                                    Icon(Icons.Default.Cancel, null)
                                }
                            }
                        else
                            AnimatedVisibility(
                                state.query.isNotEmpty(),
                                enter = materialFadeThroughIn(),
                                exit = materialFadeThroughOut()
                            ) {
                                IconButton({ onSearchEvent(SearchAction.UpdateQuery("")) }) {
                                    Icon(Icons.Default.Clear, null)
                                }
                            }
                    }
                },
                onExpandedChange = {
                    if (clientState != ClientState.ASSISTANCE_REQUESTED)
                        onSearchEvent(SearchAction.UpdateExpanded(it))
                                   },
            )
        },
        expanded = state.expanded,
        onExpandedChange = {
            if (clientState != ClientState.ASSISTANCE_REQUESTED)
                onSearchEvent(SearchAction.UpdateExpanded(it))
                           },
        modifier = modifier,
        windowInsets = SearchBarDefaults.windowInsets.add(WindowInsets(left = 8.dp, right = 8.dp)),
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(state.result?.features ?: emptyList(), { it.id }) {
                ListItem(
                    headlineContent = {
                        Text(it.properties.full_address)
                    },
                    supportingContent = {
                        Text("${it.properties.coordinates}")
                    },
                    trailingContent = {
                        Icon(
                            Icons.Default.PinDrop,
                            contentDescription = null,
                        )
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.clickable {
                        onItemClick(it)
                        onSearchEvent(SearchAction.Collapse)
                        onSearchEvent(SearchAction.UpdateQuery(""))
                    }
                )
            }
        }
    }
}