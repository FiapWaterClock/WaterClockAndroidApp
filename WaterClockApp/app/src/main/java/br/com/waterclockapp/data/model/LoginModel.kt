package br.com.waterclockapp.data.model

import br.com.waterclockapp.domain.User

data class LoginModel(
        val name: String,
        val userId: Int,
        val token: String
){
    fun toDomain(username:String, password:String): User =
            User(username, password, name, userId, token)
}