package br.com.waterclockapp.data.model

import br.com.waterclockapp.domain.User
import com.google.gson.annotations.SerializedName

data class LoginModel(
        val name: String,
        val userId: Int,
        @SerializedName("access_token")
        val token: String
){
    fun toDomain(username:String, password:String): User =
            User(username, password, name, userId, token)
}