package stenleone.nasacompose.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import stenleone.nasacompose.model.network.PictureOfTheDay.PictureOfTheDayNetworkModel
import stenleone.nasacompose.model.network.PictureOfTheDay.PictureOfTheDayResponse

interface ApiService {

    @GET(ApiConstant.PICTURE_OF_THE_DAY)
    suspend fun getPictureOfTheDay(@Query("start_date") startDate: String, @Query("end_date") endDate: String): Response<ArrayList<PictureOfTheDayNetworkModel>?>

}