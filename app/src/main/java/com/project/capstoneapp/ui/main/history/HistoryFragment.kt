package com.project.capstoneapp.ui.main.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.capstoneapp.R
import com.project.capstoneapp.adapter.ActivitiesHomeAdapter
import com.project.capstoneapp.adapter.DateHistoryAdapter
import com.project.capstoneapp.adapter.FoodHomeAdapter
import com.project.capstoneapp.databinding.FragmentHistoryBinding
import com.project.capstoneapp.data.model.DateHistory
import com.project.capstoneapp.data.model.Food
import com.project.capstoneapp.ui.ViewModelFactory


class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var spinnerAdapter: ArrayAdapter<String>

    private lateinit var _historyViewModel: HistoryViewModel
    private val historyViewModel get() = _historyViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewModel()
        setMonthSpinner()
        setDateRecyclerView()
        setFoodAndActivitiesRecyclerView()
    }

    private fun setViewModel(){
        _historyViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(requireContext())
        )[HistoryViewModel::class.java]

        historyViewModel.isLoading.observe(viewLifecycleOwner) {
            //showLoading(it)
        }

        historyViewModel.toastText.observe(viewLifecycleOwner){

        }

        historyViewModel.historyActivityResponse.observe(viewLifecycleOwner){
            it?.let{

            }
        }
    }

    private fun setMonthSpinner() {
        spinnerAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.dropdown_menu_popup_item,
            MONTH_LIST
        )

        binding.spinnerMonth.setAdapter(spinnerAdapter)
    }

    private fun setDateRecyclerView() {
        binding.rvDate.apply {
            val dateList = ArrayList<DateHistory>()

            for (i in 21..27) {
                val dateHistory = DateHistory(
                    day = getDay(i),
                    date = i
                )
                dateList.add(dateHistory)
            }

            val dateHistoryAdapter = DateHistoryAdapter(dateList)

            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = dateHistoryAdapter
            setHasFixedSize(true)
        }
    }

    private fun setFoodAndActivitiesRecyclerView() {
        val foodList = generateFoodList()

        binding.apply {
            val foodHomeAdapter = FoodHomeAdapter(foodList)
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

        var totalCal = 0

        for (food in foodList) {
            totalCal += food.foodCal
        }

        binding.tvTotalCal.text = StringBuilder("$totalCal cal")
    }

    private fun getDay(date: Int): String {
        return if (date % 22 == 0) {
            "Tue"
        } else if (date % 23 == 0) {
            "Wed"
        } else if (date % 24 == 0) {
            "Thu"
        } else if (date % 25 == 0) {
            "Fri"
        } else if (date % 26 == 0) {
            "Sat"
        } else if (date % 27 == 0) {
            "Sun"
        } else {
            "Mon"
        }
    }

    private fun generateFoodList(): ArrayList<Food> {
        val foodList = ArrayList<Food>()

        foodList.add(
            Food(
                "Burger",
                "Big Mac",
                "09.59 AM",
                540
            )
        )

        foodList.add(
            Food(
                "Burger",
                "McDouble",
                "14.02 AM",
                380
            )
        )

        foodList.add(
            Food(
                "Burger",
                "Double Cheeseburger Cheeseburger Cheeseburger",
                "18.52 AM",
                430
            )
        )

        return foodList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        val MONTH_LIST = arrayOf(
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December",
        )
    }
}