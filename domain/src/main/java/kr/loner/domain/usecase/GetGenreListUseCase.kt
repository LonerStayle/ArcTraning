package kr.loner.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kr.loner.domain.di.IoDispatcher
import kr.loner.domain.model.GenreList
import kr.loner.domain.repository.MovieRepository
import kr.loner.domain.usecase.base.NonParamCoroutineUseCase
import javax.inject.Inject

class GetGenreListUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : NonParamCoroutineUseCase<List<GenreList.GenreModel>>(dispatcher) {
    override suspend fun execute(): List<GenreList.GenreModel> =
        movieRepository.getGenreList().genreList
}