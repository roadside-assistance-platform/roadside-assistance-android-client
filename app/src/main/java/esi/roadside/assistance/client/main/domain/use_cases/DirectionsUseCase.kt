package esi.roadside.assistance.client.main.domain.use_cases

import esi.roadside.assistance.client.main.domain.models.LocationModel
import esi.roadside.assistance.client.main.domain.repository.GeocodingRepo

class DirectionsUseCase(private val repo: GeocodingRepo) {
    suspend operator fun invoke(request: Pair<LocationModel, LocationModel>) = repo.getDistance(request)
}