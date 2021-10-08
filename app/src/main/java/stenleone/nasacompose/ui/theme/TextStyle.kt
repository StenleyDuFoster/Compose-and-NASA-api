package stenleone.nasacompose.ui.theme

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType

@ExperimentalUnitApi
val TitleTextStyle = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Bold,
    fontSize = TextUnit(23f, type = TextUnitType.Sp), fontFamily = yaldeviFont)

@ExperimentalUnitApi
val MediumTextStyle = androidx.compose.ui.text.TextStyle(fontFamily = yaldeviFont)

@ExperimentalUnitApi
val BoldTextStyle = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.W400,
    fontSize = TextUnit(18f, type = TextUnitType.Sp), fontFamily = yaldeviFont)