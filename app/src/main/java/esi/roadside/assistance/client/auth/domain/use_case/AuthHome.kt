package esi.roadside.assistance.client.auth.domain.use_case

import esi.roadside.assistance.client.auth.domain.repository.AuthRepo

class AuthHome(private val authRepo: AuthRepo) {
    suspend operator fun invoke() = authRepo.authHome()
}