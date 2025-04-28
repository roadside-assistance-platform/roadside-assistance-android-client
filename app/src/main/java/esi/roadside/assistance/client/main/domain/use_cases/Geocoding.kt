package esi.roadside.assistance.client.main.domain.use_cases

import esi.roadside.assistance.client.main.domain.repository.MainRepo

class Geocoding(private val mainRepo: MainRepo) {
    suspend operator fun invoke(query: String) = mainRepo.geocoding(query)
}