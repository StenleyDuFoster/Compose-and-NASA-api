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
        const val PAGINATE_PAGE_SIZE = 5
    }

    private val listData: ArrayList<PictureOfTheDayData?> = arrayListOf()
    val listPositionCompensation = MutableLiveData(PAGINATE_PAGE_SIZE)

    val pictureData = MutableLiveData<ArrayList<PictureOfTheDayData?>>(arrayListOf())
    val pictureOfTheDayState = MutableLiveData<UiState<Boolean>>()
    private var inProgress = false
    private var lastDate: String = ""
    private var firstDate: String = ""

    init {
        getImage(decreaseDate(getDateWithCorrectFormat(), PAGINATE_PAGE_SIZE * 2), getDateWithCorrectFormat()) //  multiply by 2 because we need first load left and right pages
    }

    private fun getImage(endDate: String = DEFAULT_DATE, startDate: String = getDateWithCorrectFormat(), addToStartList: Boolean = false) {
        if (checkIfDateStringGreater(startDate, getDateWithCorrectFormat())) {
            pictureData.postValue(createEmptyLoadList(addToStartList))
            inProgress = true
            viewModelScope.launch {
                pictureOfTheDayState.postValue(UiState.Loading(0))

                when (val dataState = pictureOfTheDayRepository.getPictureOfTheDay(endDate, startDate)) {
                    is DataState.Success -> {
                        inProgress = false
                        removeEmptyLoadList(addToStartList)

                        listData.addAll(if (addToStartList) 0 else listData.size, dataState.data.also { it.reverse() })
                        if (addToStartList) {
                            listPositionCompensation.postValue(listPositionCompensation.value ?: 0 + PAGINATE_PAGE_SIZE)
                        }
                        pictureData.postValue(listData)
                        pictureOfTheDayState.postValue(UiState.Success())
                        lastDate = endDate
                    }
                    is DataState.Error -> {
                        inProgress = false
                        if (getDateWithCorrectFormat() != endDate) {
                            lastDate = endDate
                        }
                        pictureData.postValue(removeEmptyLoadList(addToStartList))
                        pictureOfTheDayState.postValue(UiState.Error(UiError(dataState.exception.message ?: "Something went wrong", 0)))
                    }
                }
            }
        }
    }

    fun getNextImage() {
        if (!inProgress) {
            if (lastDate != "") {
                getImage(decreaseDate(lastDate), decreaseDate(lastDate, 1))
            } else {
                getImage()
            }
        }
    }

    fun getPreviousImage() {
        if (!inProgress) {
            if (lastDate != "") {
                getImage(increaseDate(lastDate, 1), increaseDate(lastDate), true)
            } else {
                getImage(addToStartList = true)
            }
        }
    }

    fun setupNewDate(newDate: Date) {
        listData.clear()

        getDateWithCorrectFormat(newDate).also {
            getImage(decreaseDate(it), it)
        }

    }

    private fun createEmptyLoadList(addToStartList: Boolean = false): ArrayList<PictureOfTheDayData?> {
        listData.apply {
            repeat(PAGINATE_PAGE_SIZE) {
                add(if (addToStartList) 0 else this.size, null)
            }
            return this
        }
    }

    private fun removeEmptyLoadList(addToStartList: Boolean = false): ArrayList<PictureOfTheDayData?> {
        listData.apply {
            repeat(PAGINATE_PAGE_SIZE) {
                remove(null)
            }
            return this
        }
    }

    private fun checkIfDateStringGreater(firstDate: String, secondDate: String): Boolean {
        return try {
            val first = SimpleDateFormat(DATE_PATTERN).parse(firstDate)
            val second = SimpleDateFormat(DATE_PATTERN).parse(secondDate)

            return first.before(second) || first == second
        } catch (e: Exception) {
            false
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateWithCorrectFormat(date: Date? = null): String {
        return try {
            SimpleDateFormat(DATE_PATTERN).format(date?.time ?: Calendar.getInstance().time)
        } catch (e: Exception) {
            DEFAULT_DATE
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun decreaseDate(date: String, minusDays: Int = PAGINATE_PAGE_SIZE): String {
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

    @SuppressLint("SimpleDateFormat")
    private fun increaseDate(date: String, plusDays: Int = PAGINATE_PAGE_SIZE): String {
        return try {
            val calendarDate = Calendar.getInstance().also {
                it.time = SimpleDateFormat(DATE_PATTERN).parse(date) ?: Calendar.getInstance().time
                it.add(Calendar.DAY_OF_YEAR, plusDays)
            }
            SimpleDateFormat(DATE_PATTERN).format(calendarDate.time)
        } catch (e: Exception) {
            date
        }
    }

}