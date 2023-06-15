package com.project.capstoneapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.project.capstoneapp.R
import com.project.capstoneapp.databinding.ActivityBottomMainBinding
import com.project.capstoneapp.ui.camera.ScanActivity
import com.project.capstoneapp.ui.onboarding.OnboardingActivity


class BottomMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBottomMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBarColor()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_bottom_main)
        navView.setupWithNavController(navController)

        binding.navView.menu.getItem(2).isEnabled = false

        setListeners()
    }

    private fun setListeners() {
        binding.btnScan.setOnClickListener {
            val iScan = Intent(this, ScanActivity::class.java)
            startActivity(iScan)
        }
    }

    override fun onBackPressed() {
        if (binding.navView.selectedItemId == R.id.navigation_exercise) {
            val view = layoutInflater.inflate(R.layout.custom_dialog_alert, null)
            val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            builder.setView(view)
            builder.setCancelable(false)
            val alert: AlertDialog = builder.create()
            alert.setCanceledOnTouchOutside(false)
            alert.show()

            val btnNo = alert.findViewById<MaterialButton>(R.id.btn_no)
            val btnYes = alert.findViewById<MaterialButton>(R.id.btn_yes)

            btnNo?.setOnClickListener {
                alert.dismiss()
            }

            btnYes?.setOnClickListener {
                val iLanding = Intent(this, OnboardingActivity::class.java)
                finishAffinity()
                startActivity(iLanding)
            }
        }
    }

    private fun setStatusBarColor() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.primaryColorBlue)
    }
}