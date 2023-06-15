package com.project.capstoneapp.ui.camera

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.mystoryapp.utils.decodeBitmap
import com.dicoding.mystoryapp.utils.rotateFile
import com.project.capstoneapp.R
import com.project.capstoneapp.data.remote.response.FoodResponse
import com.project.capstoneapp.data.remote.response.ScanResponse
import com.project.capstoneapp.databinding.ActivityCalculateBinding
import com.project.capstoneapp.ui.ViewModelFactory
import kotlinx.coroutines.launch
import java.io.File

class CalculateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalculateBinding
    private lateinit var calculateViewModel: CalculateViewModel

    private var file: File? = null

    private lateinit var spinnerRestaurantAdapter: ArrayAdapter<String>
    private lateinit var spinnerFoodAdapter: ArrayAdapter<String>

    private var foodResponse = ArrayList<FoodResponse>()

    private var restaurantList = ArrayList<String>()

    private var foodList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.layoutAppBar)

        setStatusBarColor()
        setViewModel()
        setContent()
        setListeners()
    }

    private fun setViewModel() {
        calculateViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(application)
        )[CalculateViewModel::class.java]

        calculateViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        calculateViewModel.toastText.observe(this) {

        }

        calculateViewModel.scanResponse.observe(this) {
            if (it?.error.isNullOrEmpty()) {
                binding.tvFood.text = it?.hasil.toString()
                calculateViewModel.getFoodList(binding.tvFood.text.toString())
            }
        }

        calculateViewModel.foodResponse.observe(this) {
            it?.let {
                foodResponse = ArrayList(it)
                val restaurantNames: List<String?> =
                    it.map { foodList -> foodList.company }.distinct()
                restaurantList = ArrayList(restaurantNames.filterNotNull())
                val foodNames: List<String?> =
                    it.map { foodList -> foodList.menu }.distinct()
                foodList = ArrayList(foodNames.filterNotNull())

                setSpinnerItem()
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun setContent() {
        val myFile = intent.getSerializableExtra(EXTRA_PHOTO_RESULT) as File

        val isBackCamera = intent?.getBooleanExtra(EXTRA_IS_BACK_CAMERA, true) as Boolean
        val reqWidth = 400
        val reqHeight = 300

        if (intent.getStringExtra(EXTRA_SOURCE).equals("camera")) {
            val rotatedFile = rotateFile(myFile, isBackCamera, reqWidth, reqHeight)
            binding.ivPreview.setImageBitmap(rotatedFile)
        } else {
            val rotatedFile = decodeBitmap(myFile, reqWidth, reqWidth)
            binding.ivPreview.setImageBitmap(rotatedFile)
        }

        file = myFile

        lifecycleScope.launch {
            calculateViewModel.scan(myFile)
        }
    }

    private fun setSpinnerItem() {
        binding.apply {
            spinnerRestaurantAdapter = ArrayAdapter<String>(
                this@CalculateActivity,
                R.layout.dropdown_menu_popup_item,
                restaurantList
            )
            spinnerRestaurant.setAdapter(spinnerRestaurantAdapter)

            spinnerFoodAdapter = ArrayAdapter<String>(
                this@CalculateActivity,
                R.layout.dropdown_menu_popup_item,
                foodList
            )
            spinnerFood.setAdapter(spinnerFoodAdapter)
        }
    }

    private fun setListeners() {
        binding.apply {
            spinnerRestaurant.onItemSelectedListener = object :
                AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val foodNames: List<String?> = foodResponse
                        .filter { foodResponse -> foodResponse.company.equals(restaurantList.get(position)) }
                        .map { foodList -> foodList.menu }
                        .distinct()
                    foodList = ArrayList(foodNames.filterNotNull())
                    spinnerFoodAdapter.notifyDataSetChanged()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }

                override fun onItemClick(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val foodNames: List<String?> = foodResponse
                        .filter { foodResponse -> foodResponse.company.equals(restaurantList.get(position)) }
                        .map { foodList -> foodList.menu }
                        .distinct()
                    foodList = ArrayList(foodNames.filterNotNull())
                    spinnerFoodAdapter.notifyDataSetChanged()
                }
            }

            layoutAppBar.setNavigationOnClickListener {
                @Suppress("DEPRECATION")
                val alertDialogBuilder = AlertDialog.Builder(this@CalculateActivity)
                alertDialogBuilder.setTitle(getString(R.string.title_discard_scan))
                    .setMessage(getString(R.string.message_discard_scan))
                    .setCancelable(true)
                    .setNegativeButton(getString(R.string.negative_discard_scan)) { _, _ ->
                    }
                    .setPositiveButton(getString(R.string.positive_discard_scan)) { _, _ ->
                        finish()
                    }.show()
            }

            btnFinish.setOnClickListener {
                finish()
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

    override fun onDestroy() {
        super.onDestroy()
        calculateViewModel.setScanResponse(ScanResponse("", ""))
    }

    @Suppress("DEPRECATION")
    private fun setStatusBarColor() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.primaryColorBlue)
    }

    companion object {
        const val EXTRA_SOURCE = "extra_source"
        const val EXTRA_IS_BACK_CAMERA = "extra_is_back_camera"
        const val EXTRA_PHOTO_RESULT = "extra_photo_result"
        const val EXTRA_PHOTO_URI = "extra_photo_uri"
        const val CAMERA_X_RESULT = 200
    }
}