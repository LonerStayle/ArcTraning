package kr.loner.arctraining.model.mapper

import kr.loner.arctraining.model.Genre
import kr.loner.domain.model.GenreModel

fun GenreModel.toVo() = Genre(id.toString(), name)