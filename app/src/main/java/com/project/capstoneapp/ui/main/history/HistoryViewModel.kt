package com.project.capstoneapp.ui.main.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.capstoneapp.data.Repository
import com.project.capstoneapp.data.remote.request.HistoryActivityRequest
import com.project.capstoneapp.data.remote.response.HistoryActivityResponse

class HistoryViewModel(
    private val mRepository: Repository
) : ViewModel() {

    val historyActivityResponse: LiveData<HistoryActivityResponse?> = mRepository.historyActivityResponse

    val isLoading: LiveData<Boolean> = mRepository.isLoading

    val toastText: LiveData<String> = mRepository.toastText

    suspend fun getHistoryActivity(userId: String, token: String, activityRequest: HistoryActivityRequest) {
        mRepository.getHistoryActivity(userId, token, activityRequest)
    }

    companion object {
        const val TAG = "HistoryViewModel"
    }
}