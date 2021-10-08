package stenleone.nasacompose.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import dagger.hilt.android.AndroidEntryPoint
import stenleone.nasacompose.R
import stenleone.nasacompose.ui.screens.PictureOfTheDayScreen.PictureOfTheDayScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        private const val PICTURE_OF_THE_DAY_PAGE = 1
        private const val OTHER = 2
    }

    private var selectedPageInPictureOfTheDay: Int = 0

    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    @ExperimentalUnitApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            var currentPage = remember { mutableStateOf(PICTURE_OF_THE_DAY_PAGE) }

            Scaffold(
                bottomBar = {
                    createBottomNavigation(currentPage.value) {
                        currentPage.value = it
                    }
                }
            ) {
                createPager(currentPage.value)
            }
        }
    }

    @ExperimentalMaterialApi
    @ExperimentalUnitApi
    @ExperimentalPagerApi
    @Composable
    private fun createPager(currentPage: Int) {
        HorizontalPager(
            dragEnabled = false,
            state = PagerState(
                pageCount = 4,
                currentPage = currentPage
            ),
        ) { index ->
            when (this.currentPage) {
                PICTURE_OF_THE_DAY_PAGE -> {
                    PictureOfTheDayScreen(this@MainActivity).view(selectedPageInPictureOfTheDay) {
                        selectedPageInPictureOfTheDay = it
                    }
                }
                OTHER -> {
                    Text("22")
                }
            }
        }
    }

    @ExperimentalUnitApi
    @ExperimentalPagerApi
    @Composable
    private fun createBottomNavigation(currentPage: Int, selectedPage: (Int) -> Unit) {
        BottomNavigation {
            BottomNavigationItem(
                onClick = {
                    selectedPage(PICTURE_OF_THE_DAY_PAGE)
                },
                icon = {
                    Text(text = this@MainActivity.getString(R.string.picture_of_the_day))
                }, selected = currentPage == PICTURE_OF_THE_DAY_PAGE
            )
            BottomNavigationItem(
                onClick = {
                    selectedPage(OTHER)
                },
                icon = {
                    Text(text = this@MainActivity.getString(R.string.settings))
                }, selected = currentPage == OTHER
            )
        }
    }

}