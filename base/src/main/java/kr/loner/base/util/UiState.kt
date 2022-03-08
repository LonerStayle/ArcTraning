package kr.loner.base.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import java.lang.Exception


sealed class UiState<out R> {
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val exception: Exception) : UiState<Nothing>()
    object Loading : UiState<Nothing>()

    override fun toString(): String {
        return when(this){
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }

}

val UiState<*>.succeeded
    get() = this is UiState.Success && data != null

val <T> UiState<T>.data: T?
    get() = (this as? UiState.Success)?.data

fun <T> UiState<T>.successOr(fallback:T):T{
    return (this as? UiState.Success<T>)?.data?:fallback
}


inline fun <reified T> UiState<T>.updateOnSuccess(liveData: MutableLiveData<T>) {
    if (this is UiState.Success) {
        liveData.value = data
    }
}

inline fun <reified T> UiState<T>.updateOnSuccess(stateFlow: MutableStateFlow<T>) {
    if (this is UiState.Success) {
        stateFlow.value = data
    }
}

fun <T> Result<T>.toUiState():UiState<T>{
    return when{
        isFailure -> UiState.Error(exception = Exception(exceptionOrNull()))
        isSuccess -> UiState.Success(data = getOrThrow())
        else -> UiState.Loading
    }
}


