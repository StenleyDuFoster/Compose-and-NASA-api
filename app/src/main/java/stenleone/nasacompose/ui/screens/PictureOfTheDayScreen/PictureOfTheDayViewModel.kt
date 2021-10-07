package stenleone.nasacompose.ui.screens.PictureOfTheDayScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import stenleone.nasacompose.model.ui.PictureOfTheDayData
import stenleone.nasacompose.model.ui.base.DataState
import stenleone.nasacompose.repository.PictureOfTheDayRepository
import stenleone.nasacompose.ui.common.UiError
import stenleone.nasacompose.ui.common.UiState
import javax.inject.Inject

@HiltViewModel
class PictureOfTheDayViewModel @Inject constructor(
    private val pictureOfTheDayRepository: PictureOfTheDayRepository
) : ViewModel() {

    val pictureOfTheDayState = MutableLiveData<UiState<ArrayList<PictureOfTheDayData>>>()

    init {
        getImage()
    }

    fun getImage(startDate: String = "2005-05-05", endDate: String = "2005-05-05") {
        viewModelScope.launch {
            pictureOfTheDayState.postValue(UiState.Loading(0))
            val dataState = pictureOfTheDayRepository.getPictureOfTheDay(startDate, endDate)

            when(dataState) {
                is DataState.Success -> {
                    pictureOfTheDayState.postValue(UiState.Success(dataState.data))
                }
                is DataState.Error -> {
                    pictureOfTheDayState.postValue(UiState.Error(UiError(dataState.exception.toString(), 0)))
                }
            }
        }
    }

}