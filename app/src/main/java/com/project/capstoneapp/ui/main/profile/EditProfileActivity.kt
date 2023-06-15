package com.project.capstoneapp.ui.main.profile

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import com.project.capstoneapp.R
import com.project.capstoneapp.databinding.ActivityEditProfileBinding
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding

    private lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.layoutAppBar)

        setStatusBarColor()
        setDatePicker()
        setListeners()
    }

    private fun setDatePicker() {
        calendar = Calendar.getInstance()

        val date =
            DatePickerDialog.OnDateSetListener { _: DatePicker?, year: Int, month: Int, day: Int ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                updateDateLabel()
            }

        binding.edDate.setOnClickListener { v: View? ->
            DatePickerDialog(
                this,
                date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setListeners() {
        binding.apply {
            btnSave.setOnClickListener {
                finish()
            }

            layoutAppBar.setNavigationOnClickListener {
                finish()
            }
        }
    }

    private fun updateDateLabel() {
        val myFormat = "MM/dd/yy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        binding.edDate.setText(dateFormat.format(calendar.time))
    }

    private fun setStatusBarColor() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.primaryColorBlue)
    }
}