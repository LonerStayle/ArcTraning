package kr.loner.arctraining.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kr.loner.arctraining.model.mapper.toVo
import kr.loner.domain.model.NetworkResult
import kr.loner.domain.usecase.Genre
import kr.loner.domain.usecase.DiscoverQuery
import kr.loner.domain.usecase.GetGenreMovieUseCase
import timber.log.Timber
import javax.inject.Inject
import kr.loner.arctraining.model.Result

class GenreMoviePagingSource @Inject constructor(
    private val getGenreMovieUseCase: GetGenreMovieUseCase,
) : PagingSource<Int, Result>() {

    var genre = ""

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? =
        state.anchorPosition?.minus(1)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {
            val nextPage = params.key ?: 1
            val response =
                getGenreMovieUseCase.invoke(DiscoverQuery(Genre(genre), nextPage)).getOrThrow()
            LoadResult.Page(
                data = response.results
                    .filter { !it.posterPath.isNullOrEmpty() }
                    .map(NetworkResult::toVo),
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (nextPage > response.totalPages || nextPage > LIMIT_NEXT_PAGE) {
                    null
                } else {
                    response.page.plus(1)
                }
            )
        } catch (e: Exception) {
            Timber.e(e)
            LoadResult.Error(e)
        }
    }

    companion object {
        // 1 page = 20 item's
        private const val LIMIT_NEXT_PAGE = 1
    }
}