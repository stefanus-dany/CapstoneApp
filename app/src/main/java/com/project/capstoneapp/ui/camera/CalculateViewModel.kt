package com.project.capstoneapp.ui.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.capstoneapp.data.Repository
import com.project.capstoneapp.data.remote.response.FoodResponse
import com.project.capstoneapp.data.remote.response.ScanResponse
import java.io.File

class CalculateViewModel(
    private val mRepository: Repository
) : ViewModel() {
    val scanResponse: LiveData<ScanResponse?> = mRepository.scanResponse

    val foodResponse: LiveData<List<FoodResponse>?> = mRepository.foodResponse

    val isLoading: LiveData<Boolean> = mRepository.isLoading

    val toastText: LiveData<String> = mRepository.toastText

    companion object {
        const val TAG = "CalculateViewModel"
    }

    fun setScanResponse(scanResponse: ScanResponse) {
        mRepository.setScanResponse(scanResponse)
    }

    suspend fun scan(imageFile: File) {
        mRepository.scan(imageFile)
    }

    fun getFoodList(hasil: String) {
        mRepository.getFoodList(hasil)
    }
}