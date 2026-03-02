package com.example.booknookv2

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        val etUsername = findViewById<EditText>(R.id.etLoginUsername)
        val etPassword = findViewById<EditText>(R.id.etLoginPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegister = findViewById<TextView>(R.id.tvGoToRegister)
        val btnBiometric = findViewById<ImageButton>(R.id.btnBiometricLogin)



        // policing login
        viewModel.loginResult.observe(this) { isSuccess ->
            if (isSuccess) {
                val username = etUsername.text.toString()

                if (username.isNotEmpty()) {
                    val prefs = getSharedPreferences("BookNookPrefs", MODE_PRIVATE)
                    prefs.edit().putString("last_logged_in_user", username).apply()
                }

                Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }

        btnBiometric.setOnClickListener {
            showBiometricPrompt()
        }

        btnLogin.setOnClickListener {
            val user = etUsername.text.toString()
            val pass = etPassword.text.toString()

            if (user.isNotEmpty() && pass.isNotEmpty()) {
                viewModel.loginUser(user, pass)
            } else {
                Toast.makeText(this, "Please enter credentials", Toast.LENGTH_SHORT).show()
            }
        }

        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
    private fun showBiometricPrompt() {
        val executor = androidx.core.content.ContextCompat.getMainExecutor(this)

        val biometricPrompt = androidx.biometric.BiometricPrompt(this, executor,
            object : androidx.biometric.BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)

                    val prefs = getSharedPreferences("BookNookPrefs", MODE_PRIVATE)
                    val lastUser = prefs.getString("last_logged_in_user", null)
                    if (lastUser != null) {
                        viewModel.loginWithoutPassword(lastUser)
                    }
                    startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                    finish()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext, "Auth error: $errString", Toast.LENGTH_SHORT).show()
                }
            })

        val promptInfo = androidx.biometric.BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Login")
            .setSubtitle("Log in using your fingerprint or face")
            .setNegativeButtonText("Use Password")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}