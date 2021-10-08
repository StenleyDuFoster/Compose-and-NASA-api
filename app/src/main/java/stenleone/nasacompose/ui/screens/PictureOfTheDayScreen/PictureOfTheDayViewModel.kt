package stenleone.nasacompose.ui.screens.PictureOfTheDayScreen

import android.annotation.SuppressLint
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

    companion object {
        const val DATE_PATTERN = "yyyy-MM-dd"
        const val DEFAULT_DATE = "2021-05-05"
    }

    private val listData: ArrayList<PictureOfTheDayData> = arrayListOf()

    val pictureData = MutableLiveData<ArrayList<PictureOfTheDayData>>(arrayListOf())
    val pictureOfTheDayState = MutableLiveData<UiState<Boolean>>()
    private var inProgress = false
    private var lastDate: String = ""

    init {
        getImage(decreaseDate(getNowDate(), 5), getNowDate())
    }

    private fun getImage(startDate: String = DEFAULT_DATE, endDate: String = getNowDate()) {
        inProgress = true
        viewModelScope.launch {
            pictureOfTheDayState.postValue(UiState.Loading(0))

            when (val dataState = pictureOfTheDayRepository.getPictureOfTheDay(startDate, endDate)) {
                is DataState.Success -> {
                    inProgress = false
                    listData.addAll(dataState.data.also { it.reverse() })
                    pictureData.postValue(listData)
                    pictureOfTheDayState.postValue(UiState.Success())
                    lastDate = startDate
                }
                is DataState.Error -> {
                    inProgress = false
                    if (getNowDate() != startDate) {
                        lastDate = startDate
                    }
                    pictureOfTheDayState.postValue(UiState.Error(UiError(dataState.exception.message ?: "Something went wrong", 0)))
                }
            }
        }
    }

    fun getNextImage() {
        if (!inProgress) {
            if (lastDate != "") {
                getImage(decreaseDate(lastDate, 5), decreaseDate(lastDate, 1))
            } else {
                getImage()
            }
        }
    }

    fun setupNewDate() {

    }

    @SuppressLint("SimpleDateFormat")
    private fun getNowDate(): String {
        return try {
            SimpleDateFormat(DATE_PATTERN).format(Calendar.getInstance().time)
        } catch (e: Exception) {
            DEFAULT_DATE
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun decreaseDate(date: String, minusDays: Int = 0): String {
        return try {
            val calendarDate = Calendar.getInstance().also {
                it.time = SimpleDateFormat(DATE_PATTERN).parse(date) ?: Calendar.getInstance().time
                it.add(Calendar.DAY_OF_YEAR, -minusDays)
            }
            SimpleDateFormat(DATE_PATTERN).format(calendarDate.time)
        } catch (e: Exception) {
            date
        }
    }

}