package stenleone.nasacompose.ui.screens.PictureOfTheDayScreen

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import stenleone.nasacompose.R
import stenleone.nasacompose.model.ui.PictureOfTheDayData
import stenleone.nasacompose.ui.theme.MediumTextStyle
import stenleone.nasacompose.ui.theme.TitleTextStyle
import stenleone.nasacompose.ui.theme.yaldeviFont
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*
import kotlin.math.roundToInt

class PictureOfTheDayPage(private val context: Context, private val dataState: PictureOfTheDayData? = null, private val changeDateCallBack: (Date) -> Unit) {

    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalAnimationApi
    @ExperimentalUnitApi
    @Composable
    fun View() {

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

        val dialogState = rememberMaterialDialogState()
        MaterialDialog(
            dialogState = dialogState,
            buttons = {
                positiveButton("Ok")
                negativeButton("Cancel")
            }
        ) {

            datepicker { date ->



                changeDateCallBack(Date.from(date.atStartOfDay().toInstant(ZoneOffset.MAX)))
                dialogState.hide()
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
                    imageModel = dataState?.url ?: "", Modifier.fillMaxWidth(),
                    circularReveal = CircularReveal(duration = 250),
                )
                Text(
                    text = dataState?.title ?: "", modifier = Modifier.padding(5.dp), style = TitleTextStyle
                )
                Text(text = dataState?.explanation ?: "", style = MediumTextStyle, modifier = Modifier.padding(start = 5.dp, end = 5.dp, top = 0.dp, bottom = 5.dp))
                Box(modifier = Modifier.padding(bottom = toolbarHeight))
            }
            TopAppBar(
                modifier = Modifier
                    .height(toolbarHeight)
                    .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) },
                title = {
                    Text("${context.getString(R.string.photo_of_the_day)} ")
                    AnimatedVisibility(dataState?.date != null) {
                        Text("${dataState?.date ?: ""}")
                    }

                    Image(painterResource(R.drawable.ic_pen), contentDescription = "", modifier = Modifier
                        .clickable {
                            dialogState.show()
                        }
                        .padding(5.dp))
                }
            )
            if (dataState == null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }

}