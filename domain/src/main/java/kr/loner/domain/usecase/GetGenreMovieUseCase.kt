package kr.loner.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kr.loner.domain.di.IoDispatcher
import kr.loner.domain.model.MovieModel
import kr.loner.domain.repository.MovieRepository
import kr.loner.domain.usecase.base.UseCase
import javax.inject.Inject

class GetGenreMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<DiscoverQuery, MovieModel>(dispatcher) {
    override suspend fun execute(param: DiscoverQuery): MovieModel =
        movieRepository.getGenreMovies(param)
}

@JvmInline
value class Genre(val value: String)
data class DiscoverQuery(val genre: Genre, val page: Int)