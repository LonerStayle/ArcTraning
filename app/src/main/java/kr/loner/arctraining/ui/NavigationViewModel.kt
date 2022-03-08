package kr.loner.arctraining.ui

import kotlinx.coroutines.flow.Flow
import kr.loner.domain.model.GenreModel
import kr.loner.arctraining.model.Result

interface NavigationViewModel {
    fun navigate(screen: Screen)
    val navigation: Flow<Screen>

    sealed class Screen {
        data class DetailMovie(val result: Result) : Screen()
        data class GenreDetail(val genreModel: GenreModel) : Screen()
    }
}