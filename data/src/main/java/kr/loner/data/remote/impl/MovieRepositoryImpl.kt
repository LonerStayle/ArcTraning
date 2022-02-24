package kr.loner.data.remote.impl

import kr.loner.data.model.mapper.toDomainModel
import kr.loner.data.remote.MovieApi
import kr.loner.domain.model.GenreList
import kr.loner.domain.model.MovieModel
import kr.loner.domain.repository.MovieRepository
import kr.loner.domain.usecase.DiscoverQuery
import kr.loner.domain.usecase.SearchQuery
import retrofit2.await
import javax.inject.Inject


class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi
) : MovieRepository {
    override suspend fun getUpcomingMovie(page: Int): MovieModel =
        movieApi.getUpcomingMovie(page).await().toDomainModel()

    override suspend fun searchMovie(searchQuery: SearchQuery): MovieModel =
        movieApi.searchMovie(searchQuery.page, searchQuery.query).await().toDomainModel()

    override suspend fun getGenreList(): GenreList =
        movieApi.getGenreList().await().toDomainModel()

    override suspend fun getGenreMovies(discoverQuery: DiscoverQuery): MovieModel =
        movieApi.getGenreMovies(discoverQuery.page, discoverQuery.genre.value).await()
            .toDomainModel()

    override suspend fun getTrendingMovie(page: Int): MovieModel =
        movieApi.getTrendingMovie(page = page).await().toDomainModel()
}