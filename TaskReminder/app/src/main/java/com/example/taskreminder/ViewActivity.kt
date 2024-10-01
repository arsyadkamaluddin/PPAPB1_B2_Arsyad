package com.example.taskreminder

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ViewActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view)
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        findViewById<TextView>(R.id.viewJudul).text = sharedPreferences.getString("judul",null)
        findViewById<TextView>(R.id.viewUlang).text = sharedPreferences.getString("pengulangan",null)
        findViewById<TextView>(R.id.viewTanggal).text = sharedPreferences.getString("tanggal",null)
        findViewById<TextView>(R.id.viewJam).text = "${sharedPreferences.getString("jam",null)}:${sharedPreferences.getString("menit",null)}"
    }
    fun toAdd(view: View){
        val intent = Intent(this, AddActivity::class.java)
        startActivity(intent)
        finish()
    }
}