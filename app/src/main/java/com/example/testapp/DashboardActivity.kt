package com.example.testapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val userName = intent.getStringExtra("USER_NAME") ?: "User"
        findViewById<TextView>(R.id.userName).text = "Hello, $userName!"
    }
} 