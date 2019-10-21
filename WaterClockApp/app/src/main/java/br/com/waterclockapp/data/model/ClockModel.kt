package br.com.waterclockapp.data.model

import com.google.gson.annotations.SerializedName

data class ClockModel(
        @SerializedName("id") val clockId: Int,
        val activate: Boolean,
        @SerializedName("intallation_date") val installationDate: String,
        @SerializedName("serial_number") val serialNumber: String
)