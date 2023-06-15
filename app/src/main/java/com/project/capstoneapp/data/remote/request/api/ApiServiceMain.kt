package com.project.capstoneapp.data.remote.request.api

import com.project.capstoneapp.data.remote.request.FoodRequest
import com.project.capstoneapp.data.remote.request.RecommendationRequest
import com.project.capstoneapp.data.remote.request.RegisterRequest
import com.project.capstoneapp.data.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiServiceMain {
    @POST("api/user/signup")
    fun register(
        @Body registerRequest: RegisterRequest
    ): Call<RegisterResponse>

    @GET("api/user/{id}")
    fun getUserById(
        @Path("id") userId: String,
        @Header("Authorization") token: String
    ): Call<UserResponse>

    @POST("api/junklist")
    fun getFoodList(
        @Body foodRequest : FoodRequest
    ): Call<List<FoodResponse>>

    @GET("api/exercise/list")
    fun getTrackingList(): Call<List<TrackingResponse>>

    @POST("api/exercise/recommendation")
    fun getRecommendationList(
        @Body recommendationRequest: RecommendationRequest
    ): Call<List<RecommendationResponse>>

    @Multipart
    @POST("api/history/user/{id}")
    suspend fun getHistoryActivity(
        @Header("Authorization") token: String,
        @Path("id") userId: String,
        @Part ("data") activityRequest: RequestBody
    ): Response<HistoryActivityResponse>

    @GET("api/history/user/{id}")
    suspend fun getHistoryActivity(
        @Header("Authorization") token: String,
        @Path("id") userId: String,
    ): Response<List<HistoryActivityResponse>>

}

