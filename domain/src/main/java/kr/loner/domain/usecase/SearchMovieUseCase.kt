package kr.loner.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kr.loner.domain.di.IoDispatcher
import kr.loner.domain.model.MovieModel
import kr.loner.domain.repository.MovieRepository
import kr.loner.domain.usecase.base.UseCase
import javax.inject.Inject

class SearchMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<SearchQuery, MovieModel>(dispatcher) {
    override suspend fun execute(param: SearchQuery): MovieModel = movieRepository.searchMovie(param)
}

data class SearchQuery(val query: String, val page: Int)