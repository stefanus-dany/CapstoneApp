package com.project.capstoneapp.ui.main.exercise.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.capstoneapp.data.Repository
import com.project.capstoneapp.data.remote.response.TrackingResponse

class TrackingViewModel (
    private val mRepository: Repository
) : ViewModel(){

    val trackingResponse: LiveData<List<TrackingResponse>?> = mRepository.trackingResponse

    val isLoading: LiveData<Boolean> = mRepository.isLoading

    val toastText: LiveData<String> = mRepository.toastText

    fun getTrackingList() {
        mRepository.getTrackingList()
    }

    companion object {
        const val TAG = "TrackingViewModel"
    }


}

