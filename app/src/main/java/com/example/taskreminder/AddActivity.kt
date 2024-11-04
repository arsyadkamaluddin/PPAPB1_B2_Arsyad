package com.example.taskreminder

import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.taskreminder.dialog.DatePicker

class AddActivity : AppCompatActivity(), OnDateSetListener {
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add)
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val pilPengulangan: Spinner = findViewById(R.id.pilPengulangan)
        val inTanggal: LinearLayout = findViewById(R.id.inputTanggal)
        val listRepeat = listOf(
            "Sekali",
            "Harian",
            "Weekdays"
        )

        pilPengulangan.adapter =ArrayAdapter(this@AddActivity,android.R.layout.simple_list_item_1,listRepeat)
        inTanggal.setOnClickListener {
            DatePicker().show(supportFragmentManager,"datePicker")
        }



    }
    override fun onDateSet(p0: android.widget.DatePicker?, p1: Int, p2: Int, p3: Int) {
        val selectedDate = "$p1/${p2+1}/$p3"
        val viewInputTanggal: TextView = findViewById(R.id.viewInputTanggal)
        viewInputTanggal.text = selectedDate
    }

    fun showAlert(view: View){
        val pilPengulangan: Spinner = findViewById(R.id.pilPengulangan)
        val viewInputTanggal: TextView = findViewById(R.id.viewInputTanggal)
        val inputJudul: EditText = findViewById(R.id.inputJudul)
        val inputJam: EditText = findViewById(R.id.inputJam)
        val inputMenit: EditText = findViewById(R.id.inputMenit)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Task Reminder")
        builder.setMessage("Ingin menambahkan pengingat ?")
        builder.setPositiveButton("Iya"){_,_->
            if(viewInputTanggal.text!="Masukkan tanggal" && inputJudul.text.toString() != "" && inputJam.text.toString() != "" && inputMenit.text.toString() != ""){
                val editor = sharedPreferences.edit()
                editor.putString("judul", inputJudul.text.toString())
                editor.putString("pengulangan", pilPengulangan.selectedItem.toString())
                editor.putString("tanggal", viewInputTanggal.text.toString())
                editor.putString("jam", inputJam.text.toString())
                editor.putString("menit", inputMenit.text.toString())
                editor.apply()

                val intent = Intent(this, ViewActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this@AddActivity, "Isikan Tanggal, Jam, dan Judul", Toast.LENGTH_SHORT).show()
            }

        }
        builder.setNegativeButton("Batal"){dialog,_->
            dialog.dismiss()
        }
        builder.create().show()
    }
}