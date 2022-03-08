package kr.loner.arctraining.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kr.loner.arctraining.model.Result
import kr.loner.arctraining.model.mapper.toVo
import kr.loner.domain.model.NetworkResult
import kr.loner.domain.usecase.GetUpComingMovieUseCase
import timber.log.Timber
import javax.inject.Inject

class UpcomingMoviePagingSource @Inject constructor(
    private val getUpcomingMovieUseCase: GetUpComingMovieUseCase
) : PagingSource<Int, Result>() {

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? =
        state.anchorPosition?.minus(1)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {
            val nextPage = params.key ?: 1
            val response = getUpcomingMovieUseCase.invoke(nextPage).getOrThrow()
            LoadResult.Page(
                data = response.results
                    .filter { !it.posterPath.isNullOrEmpty() }
                    .map(NetworkResult::toVo),
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (nextPage > response.totalPages) null else response.page.plus(1)
            )
        } catch (e: Exception) {
            Timber.e(e)
            LoadResult.Error(e)
        }
    }
}