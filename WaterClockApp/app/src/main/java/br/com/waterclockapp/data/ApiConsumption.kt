package br.com.waterclockapp.data

import br.com.waterclockapp.data.model.ConsumptionModel
import br.com.waterclockapp.domain.Consumption
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface ApiConsumption {

    @GET("api/consumption/clock/{clock_id}/month/{month}/year/{year}")
    fun getCustomer(
            @Path("clock_id") clockId: Int,
            @Path("month") month: Int,
            @Path("year") year: Int,
            @Header("Authorization") token: String): Call<List<ConsumptionModel>>


    companion object {
        operator fun invoke () : ApiConsumption {
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
                    .create(ApiConsumption::class.java)
        }
    }
}