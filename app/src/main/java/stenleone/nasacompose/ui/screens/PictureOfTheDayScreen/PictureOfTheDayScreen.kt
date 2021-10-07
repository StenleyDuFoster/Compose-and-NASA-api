package stenleone.nasacompose.ui.screens.PictureOfTheDayScreen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import stenleone.nasacompose.R
import stenleone.nasacompose.model.ui.PictureOfTheDayData
import stenleone.nasacompose.ui.common.UiState
import kotlin.math.roundToInt

class PictureOfTheDayScreen(private val context: Context) {

    @Composable
    fun view(
        viewModel: PictureOfTheDayViewModel = hiltViewModel()
    ) {

        val dataByState = viewModel.pictureOfTheDayState.observeAsState()
        var successState: UiState.Success<ArrayList<PictureOfTheDayData>>? = null
        var errorState: UiState.Error? = null
        var loadingState: UiState.Loading? = null

        if (dataByState.value != null) {
            when (dataByState.value) {
                is UiState.Success -> {
                    successState = UiState.Success((dataByState.value as UiState.Success<ArrayList<PictureOfTheDayData>>).data)
                }
                is UiState.Loading -> {
                    loadingState = UiState.Loading((dataByState.value as UiState.Loading).type)
                }
                is UiState.Error -> {
                    errorState = UiState.Error((dataByState.value as UiState.Error).exception)
                }
            }
        }

        val toolbarHeight = 48.dp
        val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
        val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }

        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    val delta = available.y
                    val newOffset = toolbarOffsetHeightPx.value + delta
                    toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)
                    return Offset.Zero
                }
            }
        }
        Box(
            Modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection)
        ) {
            Column(Modifier.padding(top = toolbarHeight)) {
                GlideImage(
                    imageModel = successState?.data?.firstOrNull()?.url ?: "", Modifier.fillMaxWidth(),
                    error = ImageBitmap.imageResource(R.drawable.nasa_logo), circularReveal = CircularReveal(duration = 250),
                )



            }
            TopAppBar(
                modifier = Modifier
                    .height(toolbarHeight)
                    .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) },
                title = {
                    Text("${context.getString(R.string.photo_of_the_day)} ${successState?.data?.firstOrNull()?.date}")

                    Image(painterResource(R.drawable.ic_pen), contentDescription = "", modifier = Modifier
                        .clickable {
//                        Toast.makeText(context, "ff", Toast.LENGTH_LONG)
                        }
                        .padding(5.dp))
                }
            )
        }
    }
}