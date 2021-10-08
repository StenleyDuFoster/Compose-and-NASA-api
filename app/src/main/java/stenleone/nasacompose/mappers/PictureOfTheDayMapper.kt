package stenleone.nasacompose.mappers

import stenleone.nasacompose.api.ApiConstant
import stenleone.nasacompose.mappers.base.UiNetworkMapper
import stenleone.nasacompose.model.network.PictureOfTheDay.PictureOfTheDayNetworkModel
import stenleone.nasacompose.model.network.PictureOfTheDay.PictureOfTheDayResponse
import stenleone.nasacompose.model.ui.PictureOfTheDayData
import stenleone.nasacompose.model.ui.base.DataState
import stenleone.nasacompose.model.ui.base.RequestError
import stenleone.nasacompose.utils.mapToUserDate
import javax.inject.Inject

class PictureOfTheDayMapper @Inject constructor() : UiNetworkMapper<PictureOfTheDayNetworkModel, PictureOfTheDayData> {

    override fun mapFromEntity(entity: PictureOfTheDayNetworkModel): PictureOfTheDayData {
        return with(entity) {
            PictureOfTheDayData(
                date.mapToUserDate(),
                explanation ?: "",
                hdUrl ?: ApiConstant.IMAGE_404,
                mediaType ?: "text",
                serviceVersion ?: "v1",
                title ?: "Not Found",
                url ?: ApiConstant.IMAGE_404
            )
        }
    }
}