package stenleone.nasacompose.ui.screens.PictureOfTheDayScreen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import stenleone.nasacompose.R
import stenleone.nasacompose.model.ui.PictureOfTheDayData
import stenleone.nasacompose.ui.common.UiState
import stenleone.nasacompose.ui.theme.yaldeviFont
import kotlin.math.roundToInt

class PictureOfTheDayScreen(private val context: Context) {

    @ExperimentalUnitApi
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
            Column(
                Modifier
                    .verticalScroll(enabled = true, state = ScrollState(0))
                    .fillMaxSize()
                    .offset {
                        IntOffset(
                            x = 0,
                            y = toolbarHeight
                                .toPx()
                                .toInt() + toolbarOffsetHeightPx.value.roundToInt()
                        )
                    },
            ) {
                GlideImage(
                    imageModel = successState?.data?.firstOrNull()?.url ?: "", Modifier.fillMaxWidth(),
                    circularReveal = CircularReveal(duration = 250),
                )
                Text(
                    text = successState?.data?.firstOrNull()?.title ?: "", modifier = Modifier.padding(5.dp), fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(23f, type = TextUnitType.Sp), fontFamily = yaldeviFont
                )
                Text(text = successState?.data?.firstOrNull()?.explanation ?: "", fontFamily = yaldeviFont, modifier = Modifier.padding(start = 5.dp, end = 5.dp, top = 0.dp, bottom = 5.dp))

            }
            TopAppBar(
                modifier = Modifier
                    .height(toolbarHeight)
                    .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) },
                title = {
                    Text("${context.getString(R.string.photo_of_the_day)} ${successState?.data?.firstOrNull()?.date ?: ""}")

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