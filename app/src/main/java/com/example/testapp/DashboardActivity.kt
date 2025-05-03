/**
 * DashboardActivity - A welcome screen that displays personalized user information
 * 
 * This activity serves as a landing page after user authentication and provides:
 * - Personalized welcome message
 * - User information display
 * - Modern Material Design UI with card-based layout
 */
package com.example.testapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Get user name from intent extras, default to "User" if not provided
        val userName = intent.getStringExtra("USER_NAME") ?: "User"
        findViewById<TextView>(R.id.userName).text = "Hello, $userName!"
    }
} 