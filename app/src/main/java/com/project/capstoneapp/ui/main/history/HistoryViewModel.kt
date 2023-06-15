package com.project.capstoneapp.ui.main.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.capstoneapp.data.Repository
import com.project.capstoneapp.data.model.Session
import com.project.capstoneapp.data.remote.request.HistoryActivityRequest
import com.project.capstoneapp.data.remote.response.HistoryActivityResponse

class HistoryViewModel(
    private val mRepository: Repository
) : ViewModel() {

    val historyActivityResponse: LiveData<List<HistoryActivityResponse>?> = mRepository.historyActivityResponse

    val isLoading: LiveData<Boolean> = mRepository.isLoading

    val toastText: LiveData<String> = mRepository.toastText

    fun getLoginSession() : LiveData<Session> = mRepository.getLoginSession()

    suspend fun getHistoryActivity(userId: String, token: String) {
        mRepository.getHistoryActivity(userId, token)
    }

    companion object {
        const val TAG = "HistoryViewModel"
    }
}