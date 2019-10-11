package br.com.waterclockapp.data.repository

import br.com.waterclockapp.data.UserApi
import br.com.waterclockapp.data.model.LoginModel
import br.com.waterclockapp.data.model.RegisterModel
import br.com.waterclockapp.data.model.UserModel
import br.com.waterclockapp.domain.User
import br.com.waterclockapp.domain.UserContract
import br.com.waterclockapp.util.BaseCallback
import br.com.waterclockapp.util.Preferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository :UserContract.IRepository{



    override fun startLogin(username: String, password: String, onResult: BaseCallback<User>) {
       UserApi.invoke().login("password", username, password).enqueue(object : Callback<LoginModel> {
        //ServiceGenerator.createService(ApiUser::class.java, "testjwtclientid", "XY7kmzoNzl100")
          //      .basicLogin("password", username, password).enqueue(object : Callback<LoginModel> {
            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                t.message?.let { onResult.onUnsuccessful(it) }
            }

            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                if(response.code() == 401) return onResult.onUnsuccessful("Não autorizado")

                if(!response.isSuccessful) return onResult.onUnsuccessful("Não autorizado")
                if(response.body() == null) return onResult.onUnsuccessful(response.message())

                response.body()?.let {
                    return onResult.onSuccessful(it.toDomain(username, password))
                }
            }

        })
    }

    override fun getUserInformation(onResult: BaseCallback<UserModel>) {
        Preferences.getPreferences()?.let {
            UserApi.invoke().getUserInformations(it.userId!!, "bearer ${it.token!!}").enqueue(object : Callback<UserModel>{
                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    t.message?.let { message -> onResult.onUnsuccessful(message) }
                }

                override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                    if(response.code() == 401) return onResult.onUnsuccessful("Não Encontrado")

                    if(!response.isSuccessful) return onResult.onUnsuccessful("Dados não encontrados")
                    if(response.body() == null) return onResult.onUnsuccessful(response.message())

                    response.body()?.let {user ->
                        return onResult.onSuccessful(user)
                    }
                }

            })

        }

    }

    override fun createNewUser(register: RegisterModel, onResult: BaseCallback<UserModel>) {
        UserApi.invoke().createNewUser(register).enqueue(object : Callback<UserModel>{
            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                t.message?.let { onResult.onUnsuccessful(it) }
            }

            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if(response.code() == 401) return onResult.onUnsuccessful("There is an account with that email address: ${register.email}")

                if(!response.isSuccessful) return onResult.onUnsuccessful("Dados não encontrados")
                if(response.body() == null) return onResult.onUnsuccessful(response.message())

                response.body()?.let {user ->
                    return onResult.onSuccessful(user)
                }
            }

        })
    }

    override fun deleteUser(userId: Int, onResult: BaseCallback<Void>) {
        UserApi.invoke().deleteUser(userId, "bearer ${Preferences.getPreferences()?.token}")
                .enqueue(object : Callback<Void>{
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        t.message?.let { onResult.onUnsuccessful(it) }
                    }

                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if(!response.isSuccessful) return onResult.onUnsuccessful("Usuário não conseguiu ser deletado")
                       if(response.errorBody() != null) return onResult.onUnsuccessful("Usuário não conseguiu ser deletado")
                        response.body()?.let { onResult.onSuccessful(it) }
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