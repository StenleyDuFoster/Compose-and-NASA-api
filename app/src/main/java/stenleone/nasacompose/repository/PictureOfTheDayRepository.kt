package stenleone.nasacompose.repository

import stenleone.nasacompose.api.ApiService
import stenleone.nasacompose.mappers.PictureOfTheDayMapper
import stenleone.nasacompose.model.ui.PictureOfTheDayData
import stenleone.nasacompose.model.ui.base.DataState
import stenleone.nasacompose.model.ui.base.RequestError
import javax.inject.Inject

class PictureOfTheDayRepository @Inject constructor(
    private val pictureOfTheDayMapper: PictureOfTheDayMapper,
    private val apiService: ApiService
    ) {

    suspend fun getPictureOfTheDay(startDate: String, endDate: String): DataState<ArrayList<PictureOfTheDayData>> {
        return try {
            val resp = apiService.getPictureOfTheDay(startDate, endDate)
            val body = resp.body()

            if (resp.isSuccessful && body != null) {
                DataState.Success(ArrayList(body.map { pictureOfTheDayMapper.mapFromEntity(it) }))
            } else {
                DataState.Error(RequestError(resp.errorBody()?.string() ?: resp.message(), resp.code()))
            }
        } catch (e: Exception) {
            DataState.Error(RequestError(e.message, -1))
        }
    }

}