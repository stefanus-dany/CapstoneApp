package com.project.capstoneapp.ui.main.exercise.fragment.recommendationfragment

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.capstoneapp.R
import com.project.capstoneapp.data.datastore.SettingsPreferences
import com.project.capstoneapp.data.remote.request.RecommendationRequest
import com.project.capstoneapp.databinding.FragmentFormRecommendationBinding
import com.project.capstoneapp.ui.ViewModelFactory
import com.project.capstoneapp.ui.main.profile.ProfileViewModel
import kotlinx.coroutines.flow.first

class FormRecommendationFragment : Fragment() {
    private var _binding: FragmentFormRecommendationBinding? = null
    private val binding get() = _binding!!

    private var _recomendationViewModel: RecommendationViewModel? = null
    private val recommendationViewModel get() = _recomendationViewModel!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormRecommendationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewModel()
        setListeners()
    }

    private fun setViewModel() {
        _recomendationViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(requireContext())
        )[RecommendationViewModel::class.java]

        recommendationViewModel.recommendationResponse.observe(this) {
            if (it != null) {
                parentFragmentManager.beginTransaction().apply {
                    replace(
                        R.id.container_recommendation,
                        ListRecommendationFragment(),
                        ListRecommendationFragment::class.java.simpleName
                    )
                    commit()
                }
            }
        }
    }

    private fun setListeners() {
        binding.btnOk.setOnClickListener {
            val calorie = binding.edCalories.text.toString().toIntOrNull() ?: 0
            val time = binding.edTime.text.toString().toIntOrNull() ?: 0

            var userWeight = 0
            recommendationViewModel.getLoginSession().observe(this) {
                userWeight = it.user?.weightKg!!.toInt()
            }

            val recommendationRequest = RecommendationRequest(calorie, time, userWeight)
            recommendationViewModel.getRecommendationList(recommendationRequest)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}