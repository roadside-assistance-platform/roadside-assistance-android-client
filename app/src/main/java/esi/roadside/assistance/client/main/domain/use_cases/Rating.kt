package esi.roadside.assistance.client.main.domain.use_cases

import esi.roadside.assistance.client.main.domain.repository.MainRepo


class Rating(private val mainRepo: MainRepo) {
    suspend operator fun invoke(serviceId: String, rating: Double?) = mainRepo.rate(serviceId, rating)
}