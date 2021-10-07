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

class PictureOfTheDayMapper @Inject constructor() : UiNetworkMapper<ArrayList<PictureOfTheDayNetworkModel>, ArrayList<PictureOfTheDayData>> {

    override fun mapFromEntity(entity: ArrayList<PictureOfTheDayNetworkModel>): ArrayList<PictureOfTheDayData> {
//        val data = if (entity.objectData != null) {
//            arrayListOf(mapObject(entity.objectData))
//        } else if (entity.listData != null && entity.listData.isNotEmpty()) {
//            val mappedList = arrayListOf<PictureOfTheDayData>()
//
//            entity.listData.forEach {
//                mappedList.add(mapObject(it))
//            }
//
//            mappedList
//        } else {
//            null
//        }
//
//        if (data != null) {
//            return DataState.Success(data)
//        } else {
//            if (entity.error != null) {
//                return DataState.Error(RequestError(entity.error.message, entity.error.code?.toIntOrNull() ?: 0))
//            } else if (entity.message != null && entity.code != null) {
//                return DataState.Error(RequestError(entity.message, entity.code?.toIntOrNull() ?: 1))
//            } else {
//                return DataState.Error(RequestError("Problem not found", 3))
//            }
//        }
        return ArrayList(entity.map { mapObject(it) })
    }

    private fun mapObject(entity: PictureOfTheDayNetworkModel): PictureOfTheDayData {
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