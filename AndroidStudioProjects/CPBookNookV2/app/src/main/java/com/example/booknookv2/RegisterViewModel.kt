package com.example.booknookv2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository

    private val _registrationSuccess = MutableLiveData<Boolean>()
    val registrationSuccess: LiveData<Boolean> = _registrationSuccess

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    fun registerUser(username: String, email: String, password: String, imagePath: String?) =
        viewModelScope.launch {
            val newUser = User(
                username = username,
                email = email,
                passwordHash = hashPassword(password),
                profileImagePath = imagePath
            )
            repository.insertUser(newUser)

            _registrationSuccess.postValue(true)
        }

    private fun hashPassword(password: String): String {
        // Future hashing BCrypt or Argon2
        return password.hashCode().toString()
    }
}