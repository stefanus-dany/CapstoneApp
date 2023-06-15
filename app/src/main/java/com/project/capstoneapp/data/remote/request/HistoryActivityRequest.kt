package com.project.capstoneapp.data.remote.request

import com.google.gson.annotations.SerializedName

data class HistoryActivityRequest (
    @field: SerializedName("jenis")
    val jenis: String,

    @field: SerializedName("name")
    val name: String,

    @field: SerializedName("durasi_menit")
    val durasi_menit: Int,

    @field: SerializedName("calorie")
    val calorie: Int,

    @field: SerializedName("created_at")
    val created_at: String

)