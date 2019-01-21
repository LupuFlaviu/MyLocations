package com.flaviul.test.mylocations.data.api.model

import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("long")
    val long: Double,
    @SerializedName("label")
    val label: String,
    @SerializedName("address")
    val address: String
)