package br.com.waterclockapp.data

import br.com.waterclockapp.data.model.LoginModel
import br.com.waterclockapp.data.model.RegisterModel
import br.com.waterclockapp.data.model.UserModel
import br.com.waterclockapp.util.BasicAuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit



interface UserApi {

    @Headers(
        "Content-Type: application/x-www-form-urlencoded"
    )
    @FormUrlEncoded
    @POST("oauth/token")
    fun login(
            @Field("grant_type") grantType: String,
            @Field("username") username: String,
            @Field("password") password: String
    ): Call<LoginModel>

    @Headers(
            "Content-Type: application/json;charset=UTF-8"
    )
    @GET("api/user/email/{email}")
    fun getUserInformations(
            @Path("email") email: String,
            @Header("Authorization") token: String): Call<UserModel>

    @Headers(
            "Content-Type: application/json"
    )
    @POST("api/user")
    fun createNewUser(@Body model: RegisterModel): Call<UserModel>

    @Headers(
            "Content-Type: application/json"
    )
    @DELETE("api/user/{id}")
    fun deleteUser(
            @Query("id") UserId : Int,
            @Header("Authorization") token : String) : Call<Void>

    companion object {
        operator fun invoke () : UserApi {
            val interceptor = HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(3, TimeUnit.MINUTES)
                    .readTimeout(3, TimeUnit.MINUTES)
                    .writeTimeout(3, TimeUnit.MINUTES)
                    .addInterceptor(interceptor)
                    .addInterceptor(BasicAuthInterceptor("testjwtclientid", "XY7kmzoNzl100"))
                    .build()
            return Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://fiapwaterclock.herokuapp.com/")
                    .build()
                    .create(UserApi::class.java)
        }
    }
}