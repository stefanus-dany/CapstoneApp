package com.project.capstoneapp.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.project.capstoneapp.R
import com.project.capstoneapp.databinding.ActivityLoginBinding
import com.project.capstoneapp.ui.auth.register.FirstRegisterActivity
import com.project.capstoneapp.ui.main.BottomMainActivity
import com.project.capstoneapp.ui.ViewModelFactory
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setViewModel()
        setListeners()
    }

    private fun setViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(application)
        )[LoginViewModel::class.java]

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        loginViewModel.toastText.observe(this) {
            var toastText = ""
            when (it?.toString()) {
                "Login Success" -> toastText = getString(R.string.login_successful)
                "There is no user record corresponding to this identifier. The user may have been deleted." -> toastText = getString(R.string.login_invalid)
                "Request Timeout" -> toastText = getString(R.string.login_failed)
                "Unable to resolve host \"bangkit-capstone-gar.ue.r.appspot.com\": No address associated with hostname" -> toastText = getString(
                    R.string.register_failed)
            }
            if (toastText.isNotEmpty()) {
                Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        loginViewModel.login(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = loginViewModel.getFirebaseAuth().currentUser
                user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                    if (tokenTask.isSuccessful) {
                        val userId = user.uid
                        val userToken = tokenTask.result?.token
                        loginViewModel.getUserById(userId, userToken.toString())
                        loginViewModel.userResponse.observe(this) {
                            if (it != null) {
                                lifecycleScope.launch {
                                    loginViewModel.saveLoginSession(userId, userToken.toString(), it)
                                }

                                Intent(
                                    this@LoginActivity,
                                    BottomMainActivity::class.java
                                ).also { intentToMain ->
                                    intentToMain.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intentToMain)
                                    finish()
                                }
                            }
                        }
                    } else {
                        val errorMessage = tokenTask.exception?.message
                        Log.e(TAG, "Failed to retrieve ID token: $errorMessage")
                        Toast.makeText(
                            this,
                            "Failed to retrieve ID token: $errorMessage",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                val errorMessage = task.exception?.message
                Log.e(TAG, "Login failed: $errorMessage")
                Toast.makeText(
                    this,
                    "Login failed: $errorMessage",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setListeners() {
        binding.apply {
            edEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    setLoginButtonEnable()
                }

                override fun afterTextChanged(s: Editable) {}
            })

            edPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    setLoginButtonEnable()
                }

                override fun afterTextChanged(s: Editable) {}
            })

            btnLogin.setOnClickListener {
                val email = edEmail.text.toString().trim()
                val password = edPassword.text.toString().trim()
                loginUser(email, password)
            }

            btnForgetPassword.setOnClickListener {

            }

            btnRegister.setOnClickListener {
                val iRegister = Intent(this@LoginActivity, FirstRegisterActivity::class.java)
                startActivity(iRegister)
            }
        }
    }

    private fun setLoginButtonEnable() {
        binding.apply {
            btnLogin.isEnabled =
                edEmail.text != null && edPassword.text != null && edEmail.text.toString()
                    .isNotEmpty() && edPassword.text.toString()
                    .isNotEmpty() && edEmail.error == null && edPassword.error == null
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val TAG = "LoginActivity"
    }
}