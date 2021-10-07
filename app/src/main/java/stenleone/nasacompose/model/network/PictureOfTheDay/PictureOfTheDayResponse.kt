package stenleone.nasacompose.model.network.PictureOfTheDay

import stenleone.nasacompose.model.network.ErrorNetworkModel

data class PictureOfTheDayResponse(
    val objectData: PictureOfTheDayNetworkModel?,
    val listData: ArrayList<PictureOfTheDayNetworkModel>?,
    val error: ErrorNetworkModel?,
    val code: String?,
    val message: String?
)