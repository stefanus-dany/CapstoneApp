package com.project.capstoneapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class FoodResponse(
    @field:SerializedName("error")
    val error: String?,

    @field:SerializedName("id")
    val id: String?,

    @field:SerializedName("Company")
    val company: String?,

    @field:SerializedName("Menu")
    val menu: String?,

    @field:SerializedName("Calories")
    val calories: Double?,

    @field:SerializedName("Jenis")
    val jenis: String?
)