package kr.loner.arctraining.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import kr.loner.arctraining.ui.screen.NavigationScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kr.loner.base.ui.compose.setThemeContent


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setThemeContent {
            CompositionLocalProvider(
                provideMainActivityViewModel(hiltViewModel<MainActivityViewModelImpl>())
            ) {
                NavigationScreen(navController = rememberNavController())
            }
        }
    }

}