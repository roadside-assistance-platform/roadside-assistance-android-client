package esi.roadside.assistance.client.main.domain.use_cases

import esi.roadside.assistance.client.main.domain.repository.GeocodingRepo

class Geocoding(private val repo: GeocodingRepo) {
    suspend operator fun invoke(query: String) = repo.geocoding(query)
}