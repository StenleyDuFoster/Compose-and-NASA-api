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
import stenleone.nasacompose.ui.screens.PictureOfTheDayScreen.PictureOfTheDayPage
import stenleone.nasacompose.ui.screens.PictureOfTheDayScreen.PictureOfTheDayScreen
import stenleone.nasacompose.ui.screens.PictureOfTheDayScreen.PictureOfTheDayViewModel
import stenleone.nasacompose.ui.theme.NasaComposeTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @ExperimentalPagerApi
    @ExperimentalUnitApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PictureOfTheDayScreen(this).view()
        }
    }

}