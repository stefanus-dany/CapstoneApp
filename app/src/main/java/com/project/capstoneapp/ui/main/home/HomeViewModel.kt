package com.project.capstoneapp.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.capstoneapp.data.Repository
import com.project.capstoneapp.data.model.Session

class HomeViewModel(private val mRepository: Repository) : ViewModel() {
    val isLoading: LiveData<Boolean> = mRepository.isLoading

    val toastText: LiveData<String> = mRepository.toastText

    fun getLoginSession() : LiveData<Session> = mRepository.getLoginSession()

    companion object {
        const val TAG = "HomeViewModel"
    }
}