package br.com.waterclockapp.data.model

import com.google.gson.annotations.SerializedName

class UserModel(
    @SerializedName( "id") val userId: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val address: String,
    val enabled: Boolean,
    val tokenExpired: Boolean,
    val clock: List<ClockModel>
)