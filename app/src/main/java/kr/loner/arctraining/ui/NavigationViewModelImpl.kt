package kr.loner.arctraining.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kr.loner.arctraining.ui.NavigationViewModel

open class NavigationViewModelImpl : NavigationViewModel, ViewModel() {

    private val _navigation = Channel<NavigationViewModel.Screen>(capacity = Channel.CONFLATED)
    override val navigation = _navigation.receiveAsFlow()

    override fun navigate(screen: NavigationViewModel.Screen) {
        viewModelScope.launch {
            _navigation.send(screen)
        }
    }
}