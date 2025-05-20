package esi.roadside.assistance.client.main.domain.use_cases

import esi.roadside.assistance.client.main.domain.repository.MainRepo
import esi.roadside.assistance.client.main.domain.models.CompletionRequest

class Completion(private val repo: MainRepo) {
    suspend operator fun invoke(request: CompletionRequest) = repo.completionRequest(request)
}