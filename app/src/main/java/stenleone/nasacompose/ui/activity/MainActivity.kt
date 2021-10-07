package stenleone.nasacompose.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import dagger.hilt.android.AndroidEntryPoint
import stenleone.nasacompose.model.ui.PictureOfTheDayData
import stenleone.nasacompose.ui.common.UiState
import stenleone.nasacompose.ui.screens.PictureOfTheDayScreen.PictureOfTheDayScreen
import stenleone.nasacompose.ui.screens.PictureOfTheDayScreen.PictureOfTheDayViewModel
import stenleone.nasacompose.ui.theme.NasaComposeTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: PictureOfTheDayViewModel by viewModels()

    @ExperimentalPagerApi
    @ExperimentalUnitApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val pictureOfTheDayDataState = viewModel.pictureOfTheDayState.observeAsState()
            var dataState: UiState.Success<ArrayList<PictureOfTheDayData>>? = null
            var loadingState: UiState.Loading? = null
            var errorState: UiState.Error? = null
            val currentPage = remember {
                mutableStateOf(0)
            }

            pictureOfTheDayDataState.value?.also {
                when (it) {
                    is UiState.Success<ArrayList<PictureOfTheDayData>> -> {
                        dataState = it
                    }
                    is UiState.Loading -> {
                        loadingState = it
                    }
                    is UiState.Error -> {
                        errorState = it
                    }
                }
            }

            NasaComposeTheme {
                Surface(color = MaterialTheme.colors.background, contentColor = Color.Black, modifier = Modifier.fillMaxSize()) {

                    HorizontalPager(
                        state = PagerState(
                            pageCount = dataState?.data?.size ?: 0,
//                            currentPage = currentPage.value
                        ),
                    ) { index ->
                        currentPage.value = index
                        val data = dataState?.data?.get(index)

                        if (index > (dataState?.data?.size ?: 1) / 2) {
                            viewModel.getNextImage()
                        }

                        PictureOfTheDayScreen(this@MainActivity, data).view()
                    }
                }
            }
        }
    }


}