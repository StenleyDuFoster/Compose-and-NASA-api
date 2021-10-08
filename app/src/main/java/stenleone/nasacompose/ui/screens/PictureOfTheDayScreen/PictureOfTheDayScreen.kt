package stenleone.nasacompose.ui.screens.PictureOfTheDayScreen

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import stenleone.nasacompose.model.ui.PictureOfTheDayData
import stenleone.nasacompose.ui.common.UiState
import stenleone.nasacompose.ui.theme.NasaComposeTheme

class PictureOfTheDayScreen(private val context: Context) {

    @ExperimentalPagerApi
    @ExperimentalUnitApi
    @Composable
    fun view(viewModel: PictureOfTheDayViewModel = hiltViewModel()) {
        val pictureOfTheDayDataState = viewModel.pictureOfTheDayState.observeAsState()
        val dataState = viewModel.pictureData.observeAsState()
        var loadingState: UiState.Loading? = null
        var errorState: UiState.Error? = null
        val currentPage = remember {
            mutableStateOf(0)
        }

        pictureOfTheDayDataState.value?.also {
            when (it) {
                is UiState.Loading -> {
                    loadingState = it
                }
                is UiState.Error -> {
                    errorState = it
                }
                else -> Unit
            }
        }

        NasaComposeTheme {
            Surface(color = MaterialTheme.colors.background, contentColor = Color.Black, modifier = Modifier.fillMaxSize()) {

                createPager(currentPage, dataState.value ?: arrayListOf(), viewModel) {
                    currentPage.value = it
                }
            }
        }
    }

    @ExperimentalUnitApi
    @ExperimentalPagerApi
    @Composable
    private fun createPager(
        currentPage: State<Int>,
        listData: ArrayList<PictureOfTheDayData>,
        viewModel: PictureOfTheDayViewModel,
        currentPageCallBack: (Int) -> Unit) {

        HorizontalPager(
            state = PagerState(
                pageCount = listData.size,
                currentPage = if (currentPage.value > listData.size) listData.size else currentPage.value
            ),
        ) { index ->
            currentPageCallBack(this.currentPage)
            val data: PictureOfTheDayData? = listData.getOrNull(index)

            startPagination(currentPage.value, listData.size, viewModel)

            PictureOfTheDayPage(context, data).view()
        }
    }

    private fun startPagination(currentPage: Int, listSize: Int, viewModel: PictureOfTheDayViewModel) {
        if (currentPage > listSize / 2) {
            viewModel.getNextImage()
        }
    }
}