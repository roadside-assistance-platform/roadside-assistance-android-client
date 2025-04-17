package esi.roadside.assistance.client.main.domain.use_cases

import esi.roadside.assistance.client.main.domain.repository.MainRepo

class Logout(private val mainRepo: MainRepo) {
    suspend operator fun invoke() = mainRepo.logout()
}