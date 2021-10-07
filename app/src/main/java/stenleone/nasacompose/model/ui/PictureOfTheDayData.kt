package stenleone.nasacompose.model.ui

data class PictureOfTheDayData(
    val date: String,
    val explanation: String,
    val hdUrl: String,
    val mediaType: String,
    val serviceVersion: String,
    val title: String,
    val url: String
)