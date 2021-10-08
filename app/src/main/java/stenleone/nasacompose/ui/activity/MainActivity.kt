package stenleone.nasacompose.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import stenleone.nasacompose.R
import stenleone.nasacompose.ui.screens.PictureOfTheDayScreen.PictureOfTheDayScreen
import stenleone.nasacompose.ui.theme.BoldTextStyle
import stenleone.nasacompose.ui.theme.MediumTextStyle

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        private const val PICTURE_OF_THE_DAY_PAGE = 0
        private const val OTHER = 1
    }

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    @ExperimentalUnitApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val pagerState = rememberPagerState(initialPage = PICTURE_OF_THE_DAY_PAGE)

            val animatedScope = rememberCoroutineScope()

            Scaffold(
                bottomBar = {
                    CreateBottomNavigation(pagerState.currentPage) {
                        animatedScope.launch {
                            pagerState.animateScrollToPage(it)
                        }
                    }
                }
            ) {
                CreatePager(pagerState)
            }

        }
    }

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @ExperimentalUnitApi
    @ExperimentalPagerApi
    @Composable
    private fun CreatePager(pagerState: PagerState) {
        HorizontalPager(
            state = pagerState,
            count = 2,
            modifier = Modifier
        ) { index ->

            when (index) {
                PICTURE_OF_THE_DAY_PAGE -> {
                    PictureOfTheDayScreen(this@MainActivity).View()
                }
                OTHER -> {
                    Box(Modifier.fillMaxSize()) {
                        Text("22", style = MediumTextStyle)
                    }
                }
            }
        }
    }

    @ExperimentalUnitApi
    @ExperimentalPagerApi
    @Composable
    private fun CreateBottomNavigation(currentPage: Int, selectedPage: (Int) -> Unit) {
        BottomNavigation {
            BottomNavigationItem(
                onClick = {
                    selectedPage(PICTURE_OF_THE_DAY_PAGE)
                },
                icon = {
                    Text(
                        text = this@MainActivity.getString(R.string.picture_of_the_day),
                        style = BoldTextStyle,
                    )
                }, selected = currentPage == PICTURE_OF_THE_DAY_PAGE
            )
            BottomNavigationItem(
                onClick = {
                    selectedPage(OTHER)
                },
                icon = {
                    Text(
                        text = this@MainActivity.getString(R.string.settings),
                        style = BoldTextStyle,
                    )
                }, selected = currentPage == OTHER
            )
        }
    }

}