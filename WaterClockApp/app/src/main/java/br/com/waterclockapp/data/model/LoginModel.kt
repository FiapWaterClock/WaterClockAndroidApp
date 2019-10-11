package br.com.waterclockapp.data.model

import br.com.waterclockapp.domain.User
import com.google.gson.annotations.SerializedName

data class LoginModel(
        val email: String?,
        val userId: Int?,
        val isAdmin: Boolean,
        @SerializedName("access_token")
        val token: String
){
    fun toDomain(username:String, password:String): User
        = User(username, password, email ?: "", userId ?: 0, token)


}