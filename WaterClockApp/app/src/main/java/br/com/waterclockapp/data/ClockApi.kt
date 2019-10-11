package br.com.waterclockapp.data

import br.com.waterclockapp.data.model.ClockModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ClockApi {
    @Headers(
            "Content-Type: application/json;charset=UTF-8"
    )
    @GET("api/clock")
    fun getAllClocks(
            @Header("Authorization") token: String): Call<List<ClockModel>>


    @GET("api/clock/{id}")
    fun createClock(
            @Path("clock_id") clockId: Int,
            @Header("Authorization") token: String) :Call<ClockModel>

    @Headers(
            "Content-Type: application/json"
    )
    @POST("api/clock")
    fun createNewClock(
            @Body model: ClockModel,
            @Header("Authorization") token: String): Call<ClockModel>

    @Headers(
            "Content-Type: application/json"
    )
    @PUT("api/clock/{id}")
    fun updateClock(
            @Path("id") clockId: Int,
            @Body model: ClockModel,
            @Header("Authorization") token: String
    ) : Call<ClockModel>

    @Headers(
            "Content-Type: application/json"
    )
    @DELETE("api/clock/{id}")
    fun deleteClock(
            @Path("id") clockId: Int,
            @Header("Authorization") token: String
    ): Call<Void>



    companion object {
        operator fun invoke () : ClockApi {
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
                    .create(ClockApi::class.java)
        }
    }
}