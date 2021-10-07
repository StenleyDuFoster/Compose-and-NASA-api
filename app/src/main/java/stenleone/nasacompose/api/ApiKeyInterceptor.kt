package stenleone.nasacompose.api

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val build = chain.request().url().newBuilder().addQueryParameter("api_key", ApiConstant.API_KEY).build()
        return chain.proceed(chain.request().newBuilder().url(build).build())
    }

}