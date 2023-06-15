package com.project.capstoneapp.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.capstoneapp.R
import com.project.capstoneapp.adapter.ActivitiesHomeAdapter
import com.project.capstoneapp.adapter.FoodHomeAdapter
import com.project.capstoneapp.databinding.FragmentHomeBinding
import com.project.capstoneapp.ui.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var _homeViewModel: HomeViewModel? = null
    private val homeViewModel get() = _homeViewModel!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setContent()
        setViewModel()
        super.onViewCreated(view, savedInstanceState)

        setDonutChart()
        setFoodAndActivitiesRecyclerView()
    }

    private fun setContent() {
        binding.tvTodayDate.text = getString(R.string.today).plus(" ${getDateWithUserLocaleFormat()}")
    }

    private fun setViewModel() {
        _homeViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(requireContext())
        )[HomeViewModel::class.java]

        homeViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        homeViewModel.getLoginSession().observe(this) {
            if (it.isLogin) {
                binding.tvWelcomingText.text = getString(R.string.welcoming_hello).plus(" ${it.user?.name}")
            }
        }
    }

    fun getDateWithUserLocaleFormat(): String {
        val currentDate = Date()
        // val userLocale = Locale.getDefault()
        val format = SimpleDateFormat("MMMM dd", Locale.US)
        return format.format(currentDate)
    }

    private fun setDonutChart() {
        binding.apply {
            val caloryOne = 1000.0
            val caloryTwo = 2000.0
            caloryOne / caloryTwo

            tvCaloriesPercentage.text =
                StringBuilder("${caloryOne.toInt()}/${caloryTwo.toInt()} Kcal")
            donutChart.setProgress(((caloryOne / caloryTwo) * 100).toInt())
        }
    }

    private fun setFoodAndActivitiesRecyclerView() {
        binding.apply {
            val foodHomeAdapter = FoodHomeAdapter(ArrayList())
            val activitiesHomeAdapter = ActivitiesHomeAdapter()

            val dividerItemDecoration = DividerItemDecoration(
                requireContext(),
                LinearLayoutManager(requireContext()).orientation
            )

            rvFoodHome.apply {
                addItemDecoration(dividerItemDecoration)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = foodHomeAdapter
            }

            rvActivityHome.apply {
                addItemDecoration(dividerItemDecoration)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = activitiesHomeAdapter
            }
        }
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