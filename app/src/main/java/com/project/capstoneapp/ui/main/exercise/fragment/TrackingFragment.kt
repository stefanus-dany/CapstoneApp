package com.project.capstoneapp.ui.main.exercise.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.capstoneapp.R
import com.project.capstoneapp.data.remote.response.FoodResponse
import com.project.capstoneapp.data.remote.response.TrackingResponse
import com.project.capstoneapp.databinding.FragmentTrackingBinding
import com.project.capstoneapp.ui.ViewModelFactory
import com.project.capstoneapp.ui.main.home.HomeViewModel

class TrackingFragment : Fragment() {

    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!

    private lateinit var spinnerAdapter: ArrayAdapter<String>

    private var trackingResponse = ArrayList<TrackingResponse>()
    private var exerciseList = ArrayList<String>()

    private var _trackingViewModel: TrackingViewModel? = null
    private val trackingViewModel get() = _trackingViewModel!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setViewModel()
        getTrackingList()
        setSpinnerExercise()
        setExerciseHoursText()

    }

    private fun getTrackingList() {
        trackingViewModel.getTrackingList()
    }

    private fun setViewModel() {
        _trackingViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(requireContext())
        )[TrackingViewModel::class.java]

        trackingViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        trackingViewModel.toastText.observe(viewLifecycleOwner){

        }

        trackingViewModel.trackingResponse.observe(viewLifecycleOwner){
            it?.let{
                trackingResponse = ArrayList(it)
                val exerciseNames: List<String?> =
                    it.map {exerciseList -> exerciseList.judul}.distinct()
                exerciseList = ArrayList(exerciseNames.filterNotNull())

                setSpinnerExercise()
            }
        }
    }

    private fun setSpinnerExercise() {
            binding.apply {
                spinnerAdapter = ArrayAdapter<String>(
                    requireContext(),
                    R.layout.dropdown_menu_popup_item,
                    exerciseList
                )
                spinnerExercise.setAdapter(spinnerAdapter)
            }


    }

    private fun setExerciseHoursText() {
        binding.tvExercise.text = binding.spinnerExercise.text
        binding.spinnerExercise.addTextChangedListener(onTextChanged = { exerciseName, _, _, _ ->
            binding.tvExercise.text = exerciseName
        })
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}