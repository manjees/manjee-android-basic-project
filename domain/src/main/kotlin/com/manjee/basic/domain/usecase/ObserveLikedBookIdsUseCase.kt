package com.manjee.basic.domain.usecase

import com.manjee.basic.domain.repository.LikedBooksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveLikedBookIdsUseCase @Inject constructor(
    private val likedBooksRepository: LikedBooksRepository
) {
    operator fun invoke(): Flow<Set<String>> = likedBooksRepository.observeLikedBookIds()
}
