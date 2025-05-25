package esi.roadside.assistance.client.main.domain.use_cases

import esi.roadside.assistance.client.core.util.account.AccountManager
import esi.roadside.assistance.client.main.domain.repository.MainRepo

class FetchServices(private val repo: MainRepo, private val accountManager: AccountManager) {
    suspend operator fun invoke() = repo.fetchServices(accountManager.getUser()?.id ?:
        throw IllegalStateException("User not logged in"))
}