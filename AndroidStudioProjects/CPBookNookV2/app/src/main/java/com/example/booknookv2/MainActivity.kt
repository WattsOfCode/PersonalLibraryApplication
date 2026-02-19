package com.example.booknookv2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install the splash screen before calling super.onCreate()
        installSplashScreen()

        super.onCreate(savedInstanceState)

        val currentUser = UserSession.currentUser

        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        finish()
    }
}
