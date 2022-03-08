package kr.loner.arctraining.model.mapper

import kr.loner.arctraining.model.Result
import kr.loner.domain.model.NetworkResult

// View Object
fun NetworkResult.toVo() = Result(
    adult,
    backdropPath,
    genreIds,
    id,
    originalLanguage,
    originalTitle,
    overview,
    popularity,
    posterPath.toString(),
    releaseDate,
    title,
    video,
    voteAverage,
    voteCount
)