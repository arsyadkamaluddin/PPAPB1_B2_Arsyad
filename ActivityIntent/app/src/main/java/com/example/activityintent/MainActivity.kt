package com.example.activityintent

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val registerButton = findViewById<Button>(R.id.btnDaftar)
        val nameEditText = findViewById<EditText>(R.id.inputName)
        val passwordEditText = findViewById<EditText>(R.id.inputPassword)
        val emailEditText = findViewById<EditText>(R.id.inputEmail)
        val phoneEditText = findViewById<EditText>(R.id.inputNomor)
        val inputGender = findViewById<Spinner>(R.id.inputGender)

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val email = emailEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val gender = inputGender.getSelectedItem().toString()

            if (name.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Isikan semua data", Toast.LENGTH_SHORT).show()
            } else {
                val editor = sharedPreferences.edit()
                editor.putString("name", name)
                editor.putString("password", password)
                editor.putString("email", email)
                editor.putString("nomor", phone)
                editor.putString("gender", gender)
                editor.apply()

                val intent = Intent(this, SecondActivity::class.java)
                startActivity(intent)
            }
        }
        val loginTextView = findViewById<TextView>(R.id.btnToLogin)
        loginTextView.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
