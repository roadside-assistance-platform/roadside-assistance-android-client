package esi.roadside.assistance.client.main.presentation.routes.home.search

sealed interface SearchAction {
    data object Expand: SearchAction
    data object Collapse: SearchAction
    data class UpdateQuery(val query: String): SearchAction
    data class UpdateExpanded(val expanded: Boolean): SearchAction
}