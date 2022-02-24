package kr.loner.data.model.mapper

import kr.loner.data.model.DatesDto
import kr.loner.data.model.MovieDto
import kr.loner.data.model.ResultDto
import kr.loner.domain.model.Dates
import kr.loner.domain.model.MovieModel
import kr.loner.domain.model.NetworkResult


fun MovieDto.toDomainModel() = MovieModel(
    dates?.toDomainModel(), page, results.map(ResultDto::toDomainModel), totalPages, totalResults
)

fun DatesDto.toDomainModel() = Dates(maximum, minimum)
fun ResultDto.toDomainModel() = NetworkResult(
    adult,
    backdropPath,
    genreIds,
    id,
    originalLanguage,
    originalTitle,
    overview,
    popularity,
    posterPath,
    releaseDate,
    title,
    video,
    voteAverage,
    voteCount
)
