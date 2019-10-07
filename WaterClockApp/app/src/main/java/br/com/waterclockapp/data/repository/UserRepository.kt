package br.com.waterclockapp.data.repository

import br.com.waterclockapp.data.ApiUser
import br.com.waterclockapp.data.model.LoginModel
import br.com.waterclockapp.domain.User
import br.com.waterclockapp.domain.UserContract
import br.com.waterclockapp.util.BaseCallback
import br.com.waterclockapp.util.mock.FakeRestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository :UserContract.IRepository{


   override fun startLogin(username: String, password: String, onResult: BaseCallback<User>) {
        ApiUser.invoke().login("password", username, password).enqueue(object : Callback<LoginModel> {
            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                t.message?.let { onResult.onUnsuccessful(it) }
            }

            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                if(response.body() == null || !response.isSuccessful) onResult.onUnsuccessful(response.message())

                response.body()?.let {
                    onResult.onSuccessful(it.toDomain(username, password))
                }
            }

        })
    }

    /*
    override fun startLogin(username: String, password: String, onResult: BaseCallback<User>) {
        FakeRestClient.getApiInterface().mockLogin(username, password)
                .enqueue(object : Callback<LoginModel> {
                    override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                        t.message?.let { onResult.onUnsuccessful(it) }
                    }

                    override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                        if (response.body() == null ||
                                !response.isSuccessful) onResult.onUnsuccessful(response.message())

                        response.body()?.let {
                            onResult.onSuccessful(it.toDomain(username, password))
                        }
                    }
                })
    }
    */

}