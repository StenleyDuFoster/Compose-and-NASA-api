package stenleone.nasacompose.utils

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

fun String?.mapToUserDate(): String {
    if (this == null) {
        return ""
    }

    val sdfInput = SimpleDateFormat("yyyy-MM-DD", Locale.getDefault())
    val sdfOutput = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())

    try {
        return sdfOutput.format(sdfInput.parse(this))
    } catch (e: Exception) {
        println(e.message)
        return ""
    }
}