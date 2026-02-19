package com.example.booknookv2

import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class RegisterActivity : AppCompatActivity() {
    private lateinit var viewModel: RegisterViewModel
    private var selectedImageUri: String? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            selectedImageUri = uri.toString()
            findViewById<ImageView>(R.id.profilePreview).setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        viewModel.registrationSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        findViewById<Button>(R.id.btnSelectPhoto).setOnClickListener {
            pickImage.launch("image/*")
        }

        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            val user = findViewById<EditText>(R.id.etUsername).text.toString()
            val email = findViewById<EditText>(R.id.etEmail).text.toString()
            val pass = findViewById<EditText>(R.id.etPassword).text.toString()

            if (user.isNotEmpty() && pass.isNotEmpty() && email.isNotEmpty()) {
                viewModel.registerUser(user, email, pass, selectedImageUri)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}