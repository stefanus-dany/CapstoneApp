package com.project.capstoneapp.data.remote.response

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type
import com.google.gson.*


class HistoryActivityResponseDeserializer : JsonDeserializer<HistoryActivityResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): HistoryActivityResponse {
        val jsonObject = json?.asJsonObject

        val id = jsonObject?.get("id")?.asString
        val userId = jsonObject?.get("user_id")?.asString
        val durasiMenit = jsonObject?.get("durasi_menit")?.asInt
        val jenis = jsonObject?.get("jenis")?.asString
        val name = jsonObject?.get("name")?.asString
        val calorie = jsonObject?.get("calorie")?.asInt
        val restaurant = jsonObject?.get("restaurant")?.asString
        val menu = jsonObject?.get("menu")?.asString
        val quantity = jsonObject?.get("quantity")?.asInt
        val createdAt = jsonObject?.get("created_at")?.asString

        return HistoryActivityResponse(id, userId, durasiMenit, jenis, name, calorie, restaurant, menu, quantity, createdAt)
    }
}


data class HistoryActivityResponse(
    @field:SerializedName("id")
    val idActivity: String?,

    @field:SerializedName("user_id")
    val userId: String?,

    @field:SerializedName("durasi_menit")
    val durasiMenit: Int?,

    @field:SerializedName("jenis")
    val jenis: String?,

    @field:SerializedName("name")
    val name: String?,

    @field:SerializedName("calorie")
    val calorie: Int?,

    @field:SerializedName("restaurant")
    val restaurant: String?,

    @field:SerializedName("menu")
    val menu: String?,

    @field:SerializedName("quantity")
    val quantity: Int?,

    @field:SerializedName("created_at")
    val createdAt: String?
)

