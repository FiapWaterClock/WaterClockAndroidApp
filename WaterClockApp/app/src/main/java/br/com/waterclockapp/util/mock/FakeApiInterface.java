package br.com.waterclockapp.util.mock;

import br.com.waterclockapp.data.model.LoginModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FakeApiInterface {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @FormUrlEncoded
    @POST("login")
    Call<LoginModel> mockLogin(
            @Field("username") String username,
            @Field("password") String password);
}
