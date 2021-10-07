package stenleone.nasacompose.ui.common

sealed class UiState<out R> {
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val exception: UiError) : UiState<Nothing>()
    data class Loading(val type: Int) : UiState<Nothing>()
}