package kr.loner.arctraining.ui

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.savedstate.SavedStateRegistryOwner
import kr.loner.arctraining.ui.GenreDetailActivity.Companion.EXTRA_GENRE_KEY
import kr.loner.arctraining.ui.paging.GenreMoviePagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kr.loner.domain.usecase.Genre
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GenreDetailViewModel @Inject constructor(
    private val genreMoviePagingSource: GenreMoviePagingSource,
    savedStateHandle: SavedStateHandle
) : NavigationViewModelImpl() {

    val selectedGenre: Genre =
        checkNotNull(savedStateHandle.get<Genre>(EXTRA_GENRE_KEY)).also { Timber.d("genre $it") }

    val genreMovie by lazy {
        Pager(PagingConfig(pageSize = 10), pagingSourceFactory = {
            genreMoviePagingSource.apply {
                genre = selectedGenre.value
            }
        }).flow
            .cachedIn(viewModelScope)
            .catch { Timber.e(it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), PagingData.empty())
    }

}

class GenreDetailViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val genreMoviePagingSource: GenreMoviePagingSource,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel> create(
        key: String, modelClass: Class<T>, handle: SavedStateHandle
    ): T = GenreDetailViewModel(genreMoviePagingSource, handle) as T
}
