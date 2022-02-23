package kr.loner.domain.model

import kotlin.random.Random

data class MovieModel(
    val dates: Dates?,
    val page:Int,
    val results: List<NetworkResult>,
    val totalPages: Int,
    val totalResults: Int
){
    data class Dates(
        val maximum: String?,
        val minimum: String?
        )
    data class NetworkResult(
        val adult: Boolean,
        val backdropPath: String,
        val genreIds: List<Int>,
        val id: Long = Random.nextLong(Long.MAX_VALUE),
        val originalLanguage: String,
        val originalTitle: String,
        val overview: String,
        val popularity: Double,
        val posterPath: String?,
        val releaseDate: String?,
        val title: String,
        val video: Boolean,
        val voteAverage: Double,
        val voteCount: Int
    )
}


