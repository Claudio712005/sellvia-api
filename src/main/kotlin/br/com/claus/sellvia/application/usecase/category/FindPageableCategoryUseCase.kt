package br.com.claus.sellvia.application.usecase.category

import br.com.claus.sellvia.domain.annotation.UseCase
import br.com.claus.sellvia.domain.repository.CategoryRepository

@UseCase
class FindPageableCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {
}