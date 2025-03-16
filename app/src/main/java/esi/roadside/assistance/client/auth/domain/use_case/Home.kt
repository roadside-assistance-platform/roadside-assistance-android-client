package esi.roadside.assistance.client.auth.domain.use_case

import esi.roadside.assistance.client.auth.domain.models.HomeRequestModel
import esi.roadside.assistance.client.auth.domain.repository.AuthRepo

class Home(private val authRepo: AuthRepo) {
    suspend operator fun invoke() = authRepo.home()
}