package com.trackpets.app.domain.usecases

import com.trackpets.app.domain.repository.RentalRepository

class GetCategoriesUseCase(
    private val repository: RentalRepository,
) {
    suspend operator fun invoke() = repository.getCategories()
}
