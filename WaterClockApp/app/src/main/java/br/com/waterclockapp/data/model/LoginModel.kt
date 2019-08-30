package br.com.waterclockapp.data.model

import br.com.waterclockapp.domain.User

data class LoginModel(
        val agency: String,
        val balance: Double,
        val bankAccount: String,
        val name: String,
        val userId: Int
){
    fun toDomain(username:String, password:String): User =
            User(username, password, agency, balance, bankAccount, name, userId)
}