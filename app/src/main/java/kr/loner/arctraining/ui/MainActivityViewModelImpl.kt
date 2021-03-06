package kr.loner.arctraining.ui

import androidx.core.util.Supplier
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kr.loner.arctraining.ui.paging.SearchMoviePagingSource
import kr.loner.arctraining.ui.paging.TrendingMoviePagingSource
import kr.loner.arctraining.ui.paging.UpcomingMoviePagingSource
import kr.loner.arctraining.ui.screen.Mode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kr.loner.domain.usecase.GetGenreListUseCase
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModelImpl @Inject constructor(
    private val upcomingMoviePagingSource: UpcomingMoviePagingSource,
    private val searchMoviePagingSource: SearchMoviePagingSource,
    private val trendingMoviePagingSource: TrendingMoviePagingSource,
    private val getGenreListUseCase: GetGenreListUseCase
) : NavigationViewModelImpl(), MainActivityViewModel {

    private val searchStartTrigger = MutableSharedFlow<Unit>()

    override var searchQuery = ""

    private val _currentMode = MutableStateFlow(Mode.NORMAL)
    override val currentMode: StateFlow<Mode> = _currentMode

    private val _keyboardTrigger = MutableSharedFlow<Long>()
    override val keyboardTrigger: SharedFlow<Long> = _keyboardTrigger

    override fun setQuery(query: String) {
        Timber.d(query)
        this.searchQuery = query
        viewModelScope.launch {
            searchStartTrigger.emit(Unit)
        }
    }

    override val searchMovie = searchStartTrigger
        .debounce(TimeUnit.MILLISECONDS.toMillis(750))
        .filter { searchQuery.isNotEmpty() }
        .map { _keyboardTrigger.emit(System.currentTimeMillis()) }
        .flatMapLatest {
            Pager(PagingConfig(pageSize = 10)) {
                searchMoviePagingSource.apply {
                    querySupplier = Supplier { searchQuery }
                }
            }.flow
                .cachedIn(viewModelScope)
                .catch { Timber.e(it) }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), PagingData.empty())

    override fun changeMode(mode: Mode) {
        _currentMode.tryEmit(mode)
    }

    private val upComingTrigger = flow {
        emit(Unit)
        emitAll(refreshFlow)
    }

    private val refreshFlow = MutableSharedFlow<Unit>()

    override fun refreshMovie() {
        viewModelScope.launch {
            refreshFlow.emit(Unit)
            _isRefresh.emit(true)
        }
    }

    private val _isRefresh = MutableStateFlow(false)
    override val isRefresh: StateFlow<Boolean> = _isRefresh

    override val upcomingMovie = upComingTrigger.flatMapLatest {
        val elapsedTime = System.currentTimeMillis()
        Pager(PagingConfig(pageSize = 10), pagingSourceFactory = ::upcomingMoviePagingSource)
            .flow
            .cachedIn(viewModelScope)
            .catch { Timber.e(it) }
            .map {
                viewModelScope.launch {
                    val fetchTime = System.currentTimeMillis() - elapsedTime
                    if (fetchTime - elapsedTime < 300) {
                        delay(300 - fetchTime)
                        _isRefresh.emit(false)
                    }
                }
                it
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), PagingData.empty())
    }

    override val genreList = flow {
        emit(getGenreListUseCase.invoke().getOrDefault(emptyList()))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    override val trendingMovie =
        Pager(PagingConfig(pageSize = 10), pagingSourceFactory = ::trendingMoviePagingSource)
            .flow
            .cachedIn(viewModelScope)
            .catch { Timber.e(it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), PagingData.empty())
}
