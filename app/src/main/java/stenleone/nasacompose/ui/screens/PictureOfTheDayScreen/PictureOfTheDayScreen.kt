package stenleone.nasacompose.ui.screens.PictureOfTheDayScreen

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import stenleone.nasacompose.R
import stenleone.nasacompose.model.ui.PictureOfTheDayData
import stenleone.nasacompose.ui.common.UiError
import stenleone.nasacompose.ui.common.UiState
import stenleone.nasacompose.ui.theme.NasaComposeTheme
import stenleone.nasacompose.ui.theme.Purple500

class PictureOfTheDayScreen(private val context: Context) {

    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    @ExperimentalUnitApi
    @Composable
    fun View(previousSelectedPage: Int, viewModel: PictureOfTheDayViewModel = hiltViewModel(), selectedPageCallBack: (Int) -> Unit) {
        val pictureOfTheDayDataState = viewModel.pictureOfTheDayState.observeAsState()
        val dataState = viewModel.pictureData.observeAsState()
        var loadingState: UiState.Loading? = null
        var errorState: UiState.Error? = null
        val currentPage = remember { mutableStateOf(previousSelectedPage) }

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
            Surface(
                color = MaterialTheme.colors.background, contentColor = Color.Black, modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 56.dp)
            ) {

                Box(contentAlignment = Alignment.Center) {
                    CreatePager(currentPage, dataState.value ?: arrayListOf(), viewModel) {
                        currentPage.value = it
                        selectedPageCallBack(it)
                    }

                    errorState?.let {
                        CreateErrorView(it.exception, viewModel)
                    }
                }
            }
        }
    }

    @ExperimentalUnitApi
    @ExperimentalPagerApi
    @Composable
    private fun CreatePager(
        currentPage: State<Int>,
        listData: ArrayList<PictureOfTheDayData>,
        viewModel: PictureOfTheDayViewModel,
        currentPageCallBack: (Int) -> Unit
    ) {

        HorizontalPager(
            state = PagerState(
                pageCount = listData.size,
                currentPage = if (currentPage.value > listData.size) listData.size else currentPage.value
            ),
            Modifier.fillMaxSize()
        ) { index ->
            currentPageCallBack(this.currentPage)
            val data: PictureOfTheDayData? = listData.getOrNull(index)

            startPagination(currentPage.value, listData.size, viewModel)

            PictureOfTheDayPage(context, data).View()
        }
    }

    @ExperimentalMaterialApi
    @Composable
    private fun CreateErrorView(error: UiError, viewModel: PictureOfTheDayViewModel) {
        Card(Modifier.padding(10.dp), elevation = 10.dp, shape = RoundedCornerShape(20.dp)) {
            Column {
                Text(
                    text = error.message,
                    Modifier
                        .padding(10.dp)
                        .fillMaxWidth(), textAlign = TextAlign.Center
                )
                Card(onClick = {
                    viewModel.getNextImage()
                }, backgroundColor = Purple500, shape = RoundedCornerShape(0.dp)) {
                    Text(
                        text = context.getString(R.string.retry),
                        Modifier
                            .padding(10.dp)
                            .fillMaxWidth(), textAlign = TextAlign.Center
                    )
                }
            }


        }
    }

    private fun startPagination(currentPage: Int, listSize: Int, viewModel: PictureOfTheDayViewModel) {
        if (currentPage > listSize / 2) {
            viewModel.getNextImage()
        }
    }
}