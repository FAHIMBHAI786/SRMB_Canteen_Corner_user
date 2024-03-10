package com.example.srmbcanteencorner.srmbcanteencorner.funoverflow

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {

    private val SPLASH_SCREEN_DURATION = 1000 

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Delay the execution of other tasks by 1 second
        Handler(Looper.getMainLooper()).postDelayed({
            executeTasks()
        }, SPLASH_SCREEN_DURATION.toLong())
    }

    private fun executeTasks() {
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val lastActivity = sharedPref.getString("lastActivity", "Home")

        val mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        val intent = if (currentUser != null) {
            when (lastActivity) {
                "Home" -> Intent(this, MainActivity::class.java)
                // Add more cases for other activities if needed
                else -> Intent(this, MainActivity::class.java)
            }
        } else {
            Intent(this, LoginActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}
