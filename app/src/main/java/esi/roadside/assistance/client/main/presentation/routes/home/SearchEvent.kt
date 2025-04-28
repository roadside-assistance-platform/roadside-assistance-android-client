package esi.roadside.assistance.client.main.presentation.routes.home

sealed interface SearchEvent {
    data object Expand: SearchEvent
    data object Collapse: SearchEvent
    data class UpdateQuery(val query: String): SearchEvent
    data class UpdateExpanded(val expanded: Boolean): SearchEvent
}