package com.project.capstoneapp.ui.main.exercise.fragment.recommendationfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.project.capstoneapp.R
import com.project.capstoneapp.adapter.RecommendationListAdapter
import com.project.capstoneapp.databinding.FragmentListRecommendationBinding
import com.project.capstoneapp.data.model.RecommendationActivity
import com.project.capstoneapp.ui.ViewModelFactory
import com.project.capstoneapp.ui.main.profile.ProfileViewModel

class ListRecommendationFragment : Fragment() {

    private var _binding: FragmentListRecommendationBinding? = null
    private val binding get() = _binding!!

    private lateinit var recommendationListAdapter: RecommendationListAdapter

    private var _recommendationViewModel: RecommendationViewModel? = null
    private val recommendationViewModel get() = _recommendationViewModel!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListRecommendationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewModel()
        setRecommendationContent()
        setOnBackPressed()
    }

    private fun setViewModel() {
        _recommendationViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(requireContext())
        )[RecommendationViewModel::class.java]
    }

    private fun setRecommendationContent() {
        recommendationViewModel.recommendationResponse.observe(viewLifecycleOwner) { recommendationResponse ->
            val recommendationList = ArrayList<RecommendationActivity>()

            recommendationResponse?.forEach { response ->
                val activity = RecommendationActivity(
                    name = response.exercise ?: "",
                    time = response.timeMinute ?: 0,
                    image = null // Replace this with your logic to load the image
                )
                recommendationList.add(activity)
            }

            recommendationListAdapter = RecommendationListAdapter(recommendationList)
            binding.rvRecommendation.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = recommendationListAdapter
            }
        }
    }




    private fun setOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(),
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (isEnabled) {
                        isEnabled = false
                        parentFragmentManager.beginTransaction().apply {
                            replace(
                                R.id.container_recommendation,
                                FormRecommendationFragment(),
                                FormRecommendationFragment::class.java.simpleName
                            )
                            commit()
                        }
                    }
                }
            })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}