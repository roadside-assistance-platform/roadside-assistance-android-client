package esi.roadside.assistance.client.auth.domain.use_case

import esi.roadside.assistance.client.auth.domain.models.UpdateModel
import esi.roadside.assistance.client.auth.domain.repository.AuthRepo
import esi.roadside.assistance.client.core.domain.util.onSuccess
import esi.roadside.assistance.client.core.util.account.AccountManager


class Update(private val authRepo: AuthRepo, private val accountManager: AccountManager) {
    suspend operator fun invoke(updateRequest: UpdateModel) =
        authRepo.update(updateRequest).onSuccess {
            accountManager.updateUser(it)
        }
}