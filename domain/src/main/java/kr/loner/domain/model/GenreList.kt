package kr.loner.domain.model

data class GenreList(
    val genreList: List<GenreModel>
) {
    data class GenreModel(val id: Int, val name: String)
}