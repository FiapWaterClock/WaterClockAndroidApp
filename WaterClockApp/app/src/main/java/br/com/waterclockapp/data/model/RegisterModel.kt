package br.com.waterclockapp.data.model

data class RegisterModel(
        val firstName: String,
        val lastName: String,
        val email: String,
        val password: String,
        val matchingPassword: String
)
