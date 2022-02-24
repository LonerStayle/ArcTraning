package kr.loner.base.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow


private suspend fun <T> Flow<T>.set(value:T){
    when(this){
        is MutableSharedFlow -> emit(value)
        is MutableStateFlow -> emit(value)
    }
}

@Composable
fun<T> rememberFlowWithLifecycle(
    flow:Flow<T>,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState:Lifecycle.State = Lifecycle.State.STARTED
):Flow<T> = remember(flow,lifecycle){
    flow.flowWithLifecycle(lifecycle, minActiveState)
}
