package br.com.waterclockapp.data

import br.com.waterclockapp.data.model.ConsumptionModel
import br.com.waterclockapp.data.model.RateModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface ConsumptionApi {

    @Headers(
            "Content-Type: application/json;charset=UTF-8"
    )
    @GET("api/consumption/clock/{clock_id}/month/{month}/year/{year}")
    fun getCustomer(
            @Path("clock_id") clockId: Int,
            @Path("month") month: Int,
            @Path("year") year: Int,
            @Header("Authorization") token: String): Call<List<ConsumptionModel>>

    @Headers(
            "Content-Type: application/json;charset=UTF-8"
    )
    @GET("api/consumption/all/clock/{clock_id}/month/{month}/year/{year}")
    fun getCustomerAll(
            @Path("clock_id") clockId: Int,
            @Path("month") month: Int,
            @Path("year") year: Int,
            @Header("Authorization") token: String): Call<ConsumptionModel>

    @Headers(
            "Content-Type: application/json;charset=UTF-8"
    )
    @GET("api/consumption/price/clock/{clock_id}/month/{month}/year/{year}")
    fun getRateAll(
            @Path("clock_id") clockId: Int,
            @Path("month") month: Int,
            @Path("year") year: Int,
            @Header("Authorization") token: String): Call<RateModel>


    companion object {
        operator fun invoke () : ConsumptionApi {
            val interceptor = HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(3, TimeUnit.MINUTES)
                    .readTimeout(3, TimeUnit.MINUTES)
                    .writeTimeout(3, TimeUnit.MINUTES)
                    .addInterceptor(interceptor)
                    .build()
            return Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://fiapwaterclock.herokuapp.com/")
                    .build()
                    .create(ConsumptionApi::class.java)
        }
    }
}