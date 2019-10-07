package br.com.waterclockapp.data

import br.com.waterclockapp.domain.Consumption
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import java.util.concurrent.TimeUnit

interface ApiConsumption {

    @GET("customer")
    fun getCustomer(
            @Header("Authorization") token: String): Call<List<Consumption>>


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
                    .baseUrl("https://bank-app-test.herokuapp.com/api/")
                    .build()
                    .create(ApiConsumption::class.java)
        }
    }
}