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
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PictureOfTheDayViewModel @Inject constructor(
    private val pictureOfTheDayRepository: PictureOfTheDayRepository
) : ViewModel() {

    private val listData: ArrayList<PictureOfTheDayData> = arrayListOf()

    val pictureData = MutableLiveData<ArrayList<PictureOfTheDayData>>(arrayListOf())
    val pictureOfTheDayState = MutableLiveData<UiState<Boolean>>()
    private var inProgress = false
    var lastDate: String = ""

    init {
        getImage(decreaseDate(getNowDate(), 5), getNowDate())
    }

    fun getImage(startDate: String = "2021-05-05", endDate: String = getNowDate()) {
        inProgress = true
        viewModelScope.launch {
            pictureOfTheDayState.postValue(UiState.Loading(0))
            val dataState = pictureOfTheDayRepository.getPictureOfTheDay(startDate, endDate)

            when (dataState) {
                is DataState.Success -> {
                    inProgress = false
                    listData.addAll(dataState.data.also { it.reverse() })
                    pictureData.postValue(listData)
                    pictureOfTheDayState.postValue(UiState.Success(true))
                    lastDate = startDate
                }
                is DataState.Error -> {
                    inProgress = false
                    if (getNowDate() != startDate) {
                        lastDate = startDate
                    }
                    pictureOfTheDayState.postValue(UiState.Error(UiError(dataState.exception.toString(), 0)))
                }
            }
        }
    }

    fun getNextImage() {
        if (lastDate != "" && !inProgress) {
            getImage(decreaseDate(lastDate, 5), decreaseDate(lastDate, 1))
        }
    }

    private fun getNowDate(): String {
        try {
            return SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
        } catch (e: Exception) {
            return "2005-05-05"
        }
    }

    private fun decreaseDate(date: String, minusDays: Int = 0): String {
        try {
            val calendarDate = Calendar.getInstance().also {
                it.time = SimpleDateFormat("yyyy-MM-dd").parse(date)
                it.add(Calendar.DAY_OF_YEAR, -minusDays)
            }
            return SimpleDateFormat("yyyy-MM-dd").format(calendarDate.time)
        } catch (e: Exception) {
            return date
        }
    }

}