package com.example.activityintent

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ThirdActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        val name = sharedPreferences.getString("name", "N/A")
        val email = sharedPreferences.getString("email", "N/A")
        val phone = sharedPreferences.getString("nomor", "N/A")
        val gender = sharedPreferences.getString("gender", "N/A")
        val usernameTextView = findViewById<TextView>(R.id.viewUsername)
        val emailTextView = findViewById<TextView>(R.id.viewEmail)
        val phoneTextView = findViewById<TextView>(R.id.viewNomor)

        emailTextView.text = "$email"
        phoneTextView.text = "$phone"

        if(gender=="Laki-laki"){
            usernameTextView.text = "Mas $name"

        }else{
            usernameTextView.text = "Mbak $name"
        }

        val logoutButton = findViewById<Button>(R.id.btnClose)
        logoutButton.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }
}
