package stenleone.nasacompose.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import dagger.hilt.android.AndroidEntryPoint
import stenleone.nasacompose.ui.screens.PictureOfTheDayScreen.PictureOfTheDayScreen
import stenleone.nasacompose.ui.theme.NasaComposeTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @ExperimentalUnitApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NasaComposeTheme {
                Surface(color = MaterialTheme.colors.background, contentColor = Color.Black, modifier = Modifier.fillMaxSize()) {


                    PictureOfTheDayScreen(this).view()
                }
            }
        }
    }


}