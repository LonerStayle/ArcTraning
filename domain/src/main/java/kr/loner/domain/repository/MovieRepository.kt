package kr.loner.domain.repository

import kr.loner.domain.model.GenreList
import kr.loner.domain.model.MovieModel
import kr.loner.domain.usecase.DiscoverQuery
import kr.loner.domain.usecase.SearchQuery

interface MovieRepository {
    suspend fun getUpcomingMovie(page:Int):MovieModel
    suspend fun searchMovie(searchQuery:SearchQuery):MovieModel
    suspend fun getGenreList():GenreList
    suspend fun getGenreMovies(discoverQuery: DiscoverQuery): MovieModel
    suspend fun getTrendingMovie(page:Int):MovieModel
}