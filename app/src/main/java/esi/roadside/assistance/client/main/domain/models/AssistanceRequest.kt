package esi.roadside.assistance.client.main.domain.models

import esi.roadside.assistance.client.core.domain.Category
import java.util.Date

data class AssistanceRequest(
    val clientId: String,
    val location: LocationModel,
    val category: Category,
    val createdAt: Date,
)
