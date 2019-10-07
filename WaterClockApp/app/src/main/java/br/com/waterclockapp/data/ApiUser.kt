package br.com.waterclockapp.data

import br.com.waterclockapp.data.model.LoginModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface ApiUser {

    @FormUrlEncoded
    @POST("oauth/token")
    fun login(
            @Field("grant_type") grantType: String,
            @Field("user") username: String,
            @Field("password") password: String
    ): Call<LoginModel>

    companion object {
        operator fun invoke () : ApiUser {
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
                    .create(ApiUser::class.java)
        }
    }
}