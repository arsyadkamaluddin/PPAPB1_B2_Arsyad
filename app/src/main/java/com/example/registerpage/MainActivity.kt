package com.example.registerpage

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val password: EditText = findViewById(R.id.password)
        val email: EditText = findViewById(R.id.email)
        val fullname: EditText = findViewById(R.id.fullname)
        val username: EditText = findViewById(R.id.username)
        val button: Button = findViewById(R.id.button)

        button.setOnClickListener{
            Toast.makeText(applicationContext, "Halo "+username.text.toString(), Toast.LENGTH_SHORT).show()
            password.setText("")
            email.setText("")
            fullname.setText("")
            username.setText("")

        }
    }
}