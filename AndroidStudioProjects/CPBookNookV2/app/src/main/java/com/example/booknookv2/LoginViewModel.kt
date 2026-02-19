package com.example.booknookv2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    fun loginUser(username: String, password: String) {
        viewModelScope.launch {

            val hashedInput = password.hashCode().toString()

            val user = repository.login(username, hashedInput)

            if (user != null) {
                UserSession.currentUser = user
                _loginResult.postValue(true)
            } else {
                _loginResult.postValue(false)
            }
        }
    }
}