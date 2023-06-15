package com.project.capstoneapp.ui.main.history

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.project.capstoneapp.R
import com.project.capstoneapp.data.remote.response.HistoryActivityResponse
import com.project.capstoneapp.databinding.FragmentHistoryBinding
import com.project.capstoneapp.ui.ViewModelFactory
import de.codecrafters.tableview.TableView
import de.codecrafters.tableview.model.TableColumnDpWidthModel
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter
import kotlinx.coroutines.launch
import java.util.Calendar

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var _historyViewModel: HistoryViewModel
    private val historyViewModel get() = _historyViewModel

    private var historyData: List<HistoryActivityResponse> = emptyList()

    private val calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH) + 1
    private var dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

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
        setupCalendar()
    }

    @SuppressLint("SetTextI18n")
    private fun setupCalendar() {
        val monthText = if (month < 10) "0$month" else month
        val dayText = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth
        binding.spinnerMonth.setText("$monthText/$dayText/$year")
        binding.spinnerMonth.setOnClickListener {

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    this.year = year
                    this.month = month
                    this.dayOfMonth = dayOfMonth
                    val selectedMonthText = if (month < 9) "0${month + 1}" else month + 1
                    val selectedDayText = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth
                    val selectedDate = "$selectedMonthText/$selectedDayText/$year"
                    binding.spinnerMonth.setText(selectedDate)
                    setupTableView()
                }, year, month, dayOfMonth
            )

            datePickerDialog.show()

        }
    }

    private fun setViewModel() {
        _historyViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(requireContext())
        )[HistoryViewModel::class.java]

        historyViewModel.getLoginSession().observe(viewLifecycleOwner) {
            if (it != null) {
                lifecycleScope.launch {
                    historyViewModel.getHistoryActivity(it.userId, it.token)
                }
            } else {
                Toast.makeText(requireContext(), "Try to relogin!", Toast.LENGTH_SHORT).show()
            }
        }

        historyViewModel.isLoading.observe(viewLifecycleOwner) {
            //showLoading(it)
        }

        historyViewModel.toastText.observe(viewLifecycleOwner) {

        }

        historyViewModel.historyActivityResponse.observe(viewLifecycleOwner) { data ->
            data?.let {
                historyData = it
                setupTableView()
            }
        }
    }

    private fun setupTableView() {
        setupFoodTable()
        setupExerciseTable()
    }

    private fun setupFoodTable() {
        val foodData = mutableListOf<HistoryActivityResponse>()
        val selectedMonthText = if (month < 10) "0$month" else month
        val selectedDayText = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth
        historyData.filter {
            val dateMonthFilter = it.createdAt?.split("/")
            it.jenis == "food" && dateMonthFilter?.get(0)
                .orEmpty() == selectedMonthText.toString() && dateMonthFilter?.get(1)
                .orEmpty() == selectedDayText.toString()
        }.run {
            forEach {
                foodData.add(it)
            }
        }
        val model = mutableListOf<Array<String>>()
        foodData.forEach {
            model.add(
                arrayOf(
                    it.menu.toString(),
                    it.restaurant.toString(),
                    it.calorie.toString()
                )
            )
        }
        val columnModel = TableColumnDpWidthModel(context, 3, 130)
        val exerciseHeader = listOf("Menu", "Restaurant", "Calorie")
        val tableFood: TableView<Array<String>> =
            binding.tableViewFood as TableView<Array<String>>
        tableFood.run {
            this.columnModel = columnModel
            headerAdapter =
                SimpleTableHeaderAdapter(requireContext(), *exerciseHeader.toTypedArray())
            dataAdapter = SimpleTableDataAdapter(requireContext(), model)
            setHeaderBackgroundColor(resources.getColor(R.color.primaryColorYellow))
        }
    }

    private fun setupExerciseTable() {
        val exerciseData = mutableListOf<HistoryActivityResponse>()
        val selectedMonthText = if (month < 10) "0$month" else month
        val selectedDayText = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth
        historyData.filter {
            val dateMonthFilter = it.createdAt?.split("/")
            it.jenis == "exercise" && dateMonthFilter?.get(0)
                .orEmpty() == selectedMonthText.toString() && dateMonthFilter?.get(1)
                .orEmpty() == selectedDayText.toString()
        }.run {
            forEach {
                exerciseData.add(it)
            }
        }
        val model = mutableListOf<Array<String>>()
        exerciseData.forEach {
            model.add(arrayOf(it.name.toString(), it.durasiMenit.toString(), it.calorie.toString()))
        }
        val columnModel = TableColumnDpWidthModel(context, 3, 130)
        val exerciseHeader = listOf("Activity", "Durasi (Menit)", "Calorie")
        val tableExercise: TableView<Array<String>> =
            binding.tableViewExercise as TableView<Array<String>>
        tableExercise.run {
            this.columnModel = columnModel
            headerAdapter =
                SimpleTableHeaderAdapter(requireContext(), *exerciseHeader.toTypedArray())
            dataAdapter = SimpleTableDataAdapter(requireContext(), model)
            setHeaderBackgroundColor(resources.getColor(R.color.primaryColorYellow))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}