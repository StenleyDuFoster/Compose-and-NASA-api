package stenleone.nasacompose.ui.common

sealed class UiState<out R> {
    class Success : UiState<Nothing>()
    data class Error(val exception: UiError) : UiState<Nothing>()
    data class Loading(val type: Int) : UiState<Nothing>()
}