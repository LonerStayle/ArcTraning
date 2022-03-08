package kr.loner.arctraining.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kr.loner.arctraining.ui.screen.GenreDetailScreen
import dagger.hilt.android.AndroidEntryPoint
import kr.loner.arctraining.ui.paging.GenreMoviePagingSource
import kr.loner.base.ui.compose.setThemeContent
import javax.inject.Inject

@AndroidEntryPoint
class GenreDetailActivity : AppCompatActivity() {

    @Inject
    lateinit var genreMoviePagingSource: GenreMoviePagingSource

    private val viewModel by viewModels<GenreDetailViewModel> {
        GenreDetailViewModelFactory(this, genreMoviePagingSource, intent?.extras)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setThemeContent {
            GenreDetailScreen(viewModel)
        }
    }


    companion object {
        const val EXTRA_GENRE_KEY = "extra_genre_key"
    }
}