package com.project.capstoneapp.data

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.project.capstoneapp.data.datastore.SettingsPreferences
import com.project.capstoneapp.data.model.Session
import com.project.capstoneapp.data.remote.request.FoodRequest
import com.project.capstoneapp.data.remote.request.HistoryActivityRequest
import com.project.capstoneapp.data.remote.request.RecommendationRequest
import com.project.capstoneapp.data.remote.request.RegisterRequest
import com.project.capstoneapp.data.remote.request.api.ApiServiceMachineLearning
import com.project.capstoneapp.data.remote.request.api.ApiServiceMain
import com.project.capstoneapp.data.remote.response.*
import com.project.capstoneapp.ui.auth.login.LoginViewModel
import com.project.capstoneapp.ui.auth.register.RegisterViewModel
import com.project.capstoneapp.ui.camera.CalculateViewModel
import com.project.capstoneapp.ui.main.exercise.fragment.TrackingViewModel
import com.project.capstoneapp.ui.main.exercise.fragment.recommendationfragment.RecommendationViewModel
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class Repository(
    private val settingsPreferences: SettingsPreferences,
    private val apiServiceMain: ApiServiceMain,
    private val apiServiceMachineLearning: ApiServiceMachineLearning
) {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _registerResponse = MutableLiveData<RegisterResponse?>()
    val registerResponse: LiveData<RegisterResponse?> = _registerResponse

    private val _userResponse = MutableLiveData<UserResponse?>()
    val userResponse: LiveData<UserResponse?> = _userResponse

    private val _scanResponse = MutableLiveData<ScanResponse?>()
    val scanResponse: LiveData<ScanResponse?> = _scanResponse

    private val _foodResponse = MutableLiveData<List<FoodResponse>?>()
    val foodResponse: LiveData<List<FoodResponse>?> = _foodResponse

    private val _trackingResponse = MutableLiveData<List<TrackingResponse>?>()
    val trackingResponse: LiveData<List<TrackingResponse>?> = _trackingResponse

    private val _recommendationResponse = MutableLiveData<List<RecommendationResponse>?>()
    val recommendationResponse: LiveData<List<RecommendationResponse>?> = _recommendationResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastText = MutableLiveData<String>()
    val toastText: LiveData<String> = _toastText

    private val _historyActivityResponse = MutableLiveData<List<HistoryActivityResponse>?>()
    val historyActivityResponse: LiveData<List<HistoryActivityResponse>?> = _historyActivityResponse

    fun register(
        name: String,
        email: String,
        password: String,
        gender: String,
        weightKg: String,
        heightCm: String,
        birthDate: String
    ) {
        _isLoading.value = true
        _toastText.value = ""
        val registerRequest = RegisterRequest(
            name = name,
            email = email,
            password = password,
            gender = gender,
            weightKg = weightKg,
            heightCm = heightCm,
            birthDate = birthDate
        )
        try {
            apiServiceMain.register(registerRequest).enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val registerResponse = response.body()
                        if (registerResponse?.error != null) {
                            _toastText.value =
                                "The email address is already in use by another account."
                            Log.e(RegisterViewModel.TAG, "onFailure: ${registerResponse.error}")
                        } else {
                            _registerResponse.value = registerResponse
                            _toastText.value = "User Created"
                            Log.d(RegisterViewModel.TAG, "onSuccess: User Created")
                        }
                    } else {
                        _toastText.value = response.message()
                        Log.e(RegisterViewModel.TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    _isLoading.value = false
                    _toastText.value = t.message.toString()
                    Log.e(RegisterViewModel.TAG, "onFailure: ${t.message}")
                }
            })
        } catch (e: Exception) {
            _isLoading.value = false
            _toastText.value = e.message.toString()
            Log.e(RegisterViewModel.TAG, "onFailure: ${e.message}")
        }
    }

    fun login(email: String, password: String): Task<AuthResult> {
        _isLoading.value = true
        _toastText.value = ""
        return auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _isLoading.value = false
                _toastText.value = "Login Success"
                Log.e(
                    LoginViewModel.TAG,
                    "onSuccess: ${task.result}"
                )
            } else {
                _isLoading.value = false
                _toastText.value = task.exception?.message.toString()
                Log.e(
                    LoginViewModel.TAG,
                    "onFailure: ${task.exception?.message.toString()}"
                )
            }
        }
    }


    fun getUserById(userId: String, token: String) {
        _isLoading.value = true
        _toastText.value = ""
        try {
            apiServiceMain.getUserById(userId, "Bearer $token")
                .enqueue(object : Callback<UserResponse> {
                    override fun onResponse(
                        call: Call<UserResponse>,
                        response: Response<UserResponse>
                    ) {
                        if (response.isSuccessful) {
                            val userResponse = response.body()
                            if (userResponse?.error != null) {
                                _toastText.value = userResponse.error.toString()
                                Log.e(
                                    LoginViewModel.TAG,
                                    "onFailure: ${userResponse.error}"
                                )
                            } else {
                                _userResponse.value = userResponse
                                _toastText.value = "User Found"
                                Log.d(
                                    LoginViewModel.TAG,
                                    "onSuccess: User Found ${response.body()?.name.toString()}"
                                )
                            }
                        } else {
                            _toastText.value = "Find User request failed"
                            Log.e(LoginViewModel.TAG, "onFailure: ${response.message()}")
                        }
                        _isLoading.value = false
                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        _toastText.value = t.message.toString()
                        Log.e(LoginViewModel.TAG, "onFailure: ${t.message}")
                        _isLoading.value = false
                    }
                })
        } catch (e: Exception) {
            _isLoading.value = false
            _toastText.value = e.message.toString()
            Log.e(LoginViewModel.TAG, "onFailure: ${e.message}")
        }
    }

    suspend fun scan(imageFile: File) {
        _isLoading.value = true
        _toastText.value = ""
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val photoMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            imageFile.name,
            requestImageFile
        )
        try {
            val token = getToken()
            val response = apiServiceMachineLearning.scan("Bearer $token", photoMultipart)
            if (response.isSuccessful) {
                val scanResponse = response.body()
                if (scanResponse?.error != null) {
                    _toastText.value = scanResponse.error.toString()
                    Log.e(CalculateViewModel.TAG, "onFailure: ${scanResponse.error}")
                } else {
                    _scanResponse.value = scanResponse
                    Log.d(CalculateViewModel.TAG, "onSuccess: ${scanResponse?.hasil}")
                }
            } else {
                _toastText.value = response.message()
                Log.e(CalculateViewModel.TAG, "onFailure: ${response.message()}")
            }
        } catch (e: Exception) {
            _toastText.value = e.message.toString()
            Log.e(CalculateViewModel.TAG, "onFailure: ${e.message}")
        } finally {
            _isLoading.value = false
        }
    }

    fun getFoodList(hasil: String) {
        _isLoading.value = true
        _toastText.value = ""
        val foodRequest = FoodRequest(hasil)
        try {
            apiServiceMain.getFoodList(foodRequest).enqueue(object : Callback<List<FoodResponse>> {
                override fun onResponse(
                    call: Call<List<FoodResponse>>,
                    response: Response<List<FoodResponse>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val foodResponse = response.body()
                        if (foodResponse != null) {
                            _foodResponse.value = foodResponse
                            Log.d(CalculateViewModel.TAG, "onSuccess: Food list fetched successfully")
                        } else {
                            _toastText.value = "Null response received"
                            Log.e(CalculateViewModel.TAG, "onFailure: Null response received")
                        }
                    } else {
                        _toastText.value = response.message()
                        Log.e(CalculateViewModel.TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<FoodResponse>>, t: Throwable) {
                    _isLoading.value = false
                    _toastText.value = t.message.toString()
                    Log.e(CalculateViewModel.TAG, "onFailure: ${t.message.toString()}")
                }
            })
        } catch (e: Exception) {
            _isLoading.value = false
            _toastText.value = e.message.toString()
            Log.e(CalculateViewModel.TAG, "onFailure: ${e.message}")
        }
    }

    fun getTrackingList() {
        _isLoading.value = true
        _toastText.value = ""

        try {
            apiServiceMain.getTrackingList().enqueue(object : Callback<List<TrackingResponse>> {
                override fun onResponse(
                    call: Call<List<TrackingResponse>>,
                    response: Response<List<TrackingResponse>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val trackingResponse = response.body()
                        _trackingResponse.value = trackingResponse
                        Log.d(TrackingViewModel.TAG, "onSuccess: Tracking list fetched successfully")
                    } else {
                        _toastText.value = response.message()
                        Log.e(TrackingViewModel.TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<TrackingResponse>>, t: Throwable) {
                    _isLoading.value = false
                    _toastText.value = t.message.toString()
                    Log.e(TrackingViewModel.TAG, "onFailure: ${t.message.toString()}")
                }
            })
        } catch (e: Exception) {
            _isLoading.value = false
            _toastText.value = e.message.toString()
            Log.e(TrackingViewModel.TAG, "onFailure: ${e.message}")
        }
    }

    fun getRecommendationList(recommendationRequest: RecommendationRequest){
        _isLoading.value = true
        _toastText.value = ""

        try {
            apiServiceMain.getRecommendationList(recommendationRequest).enqueue(object : Callback<List<RecommendationResponse>> {
                override fun onResponse(
                    call: Call<List<RecommendationResponse>>,
                    response: Response<List<RecommendationResponse>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val recommendationResponse = response.body()
                        _recommendationResponse.value = recommendationResponse
                        Log.d(RecommendationViewModel.TAG, "onSuccess: Recommendation list fetched successfully")
                    } else {
                        _toastText.value = response.message()
                        Log.e(RecommendationViewModel.TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<RecommendationResponse>>, t: Throwable) {
                    _isLoading.value = false
                    _toastText.value = t.message.toString()
                    Log.e(RecommendationViewModel.TAG, "onFailure: ${t.message.toString()}")
                }
            })
        } catch (e: Exception) {
            _isLoading.value = false
            _toastText.value = e.message.toString()
            Log.e(RecommendationViewModel.TAG, "onFailure: ${e.message}")
        }
    }

    // for getting the history (method get)
    suspend fun getHistoryActivity(
        userId: String,
        token: String,
    ) {
        _isLoading.value = true
        _toastText.value = ""

        try {
            val response = apiServiceMain.getHistoryActivity( "Bearer $token", userId)
            if (response.isSuccessful) {
                val historyActivityResponse = response.body()
                if (historyActivityResponse != null) {
                    _historyActivityResponse.value = historyActivityResponse
                    Log.d(TAG, "onSuccess: History activity created")
                } else {
                    _toastText.value = "Failed to create history activity"
                    Log.e(TAG, "onFailure: Failed to create history activity")
                }
            } else {
                _toastText.value = response.message()
                Log.e(TAG, "onFailure: ${response.message()}")
            }
        } catch (e: Exception) {
            _toastText.value = e.message.toString()
            Log.e(TAG, "onFailure: ${e.message}")
        } finally {
            _isLoading.value = false
        }
    }


    fun setScanResponse(scanResponse: ScanResponse) {
        _scanResponse.value = scanResponse
    }

    fun getFirebaseAuth(): FirebaseAuth = auth

    suspend fun getToken(): String = settingsPreferences.getLoginSession().first().token

    fun getLoginSession(): LiveData<Session> {
        return settingsPreferences.getLoginSession().asLiveData()
    }

    suspend fun saveLoginSession(userId: String, userToken: String, user: UserResponse) {
        settingsPreferences.saveLoginSession(userId, userToken, user)
    }

    suspend fun clearLoginSession() {
        settingsPreferences.clearLoginSession()
    }

    fun logout() {
        _isLoading.value = true
        auth.signOut()
        _isLoading.value = false
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            preferences: SettingsPreferences,
            apiServiceMain: ApiServiceMain,
            apiServiceMachineLearning: ApiServiceMachineLearning
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(preferences, apiServiceMain, apiServiceMachineLearning)
            }.also { instance = it }
    }
}