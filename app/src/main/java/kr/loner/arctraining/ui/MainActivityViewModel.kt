package kr.loner.arctraining.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.paging.PagingData
import kr.loner.arctraining.ui.screen.Mode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kr.loner.domain.model.GenreModel
import kr.loner.arctraining.model.Result

interface MainActivityViewModel : NavigationViewModel {

    fun setQuery(query: String)
    val currentMode: StateFlow<Mode>
    fun changeMode(mode: Mode)
    val upcomingMovie: Flow<PagingData<Result>>
    val trendingMovie: Flow<PagingData<Result>>
    val searchMovie: StateFlow<PagingData<Result>>
    val genreList: StateFlow<List<GenreModel>>
    fun refreshMovie()
    val isRefresh: StateFlow<Boolean>
    val keyboardTrigger: SharedFlow<Long>
    var searchQuery: String

}

private val MainActivityViewModelProvider = compositionLocalOf<MainActivityViewModel> {
    noLocalProvidedFor("MainActivityViewModel")
}

fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}

fun provideMainActivityViewModel(mainActivityViewModel: MainActivityViewModel) =
    MainActivityViewModelProvider provides mainActivityViewModel

@Composable
fun currentViewModel() = MainActivityViewModelProvider.current