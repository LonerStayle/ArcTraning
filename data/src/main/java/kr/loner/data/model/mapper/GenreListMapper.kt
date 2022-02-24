package kr.loner.data.model.mapper

import kr.loner.data.model.GenreDto
import kr.loner.data.model.GenreListDto
import kr.loner.domain.model.GenreList
import kr.loner.domain.model.GenreModel

//fun GenreListDto.toDomainModel() = GenreList(genres.map(GenreDto::toDomainModel))
//fun GenreDto.toDomainModel() = GenreModel(id,name)

fun GenreListDto.toDomainModel() = GenreList(genres.map(GenreDto::toDomainModel))
fun GenreDto.toDomainModel() = GenreModel(id, name)