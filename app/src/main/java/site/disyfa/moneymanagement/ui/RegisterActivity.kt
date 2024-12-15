package site.disyfa.moneymanagement.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import site.disyfa.moneymanagement.R
import site.disyfa.moneymanagement.localdb.AppRoomDatabase
import site.disyfa.moneymanagement.localdb.UserDao
import site.disyfa.moneymanagement.model.ApiUser
import site.disyfa.moneymanagement.model.User
import site.disyfa.moneymanagement.network.ApiClient
import site.disyfa.moneymanagement.network.SingleResponse
import site.disyfa.moneymanagement.util.StringGenerator
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RegisterActivity : AppCompatActivity() {
    private lateinit var mUserDao: UserDao
    private lateinit var executorService: ExecutorService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        executorService = Executors.newSingleThreadExecutor()
        val db = AppRoomDatabase.getDatabase(this)
        mUserDao = db!!.userDao()!!
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        val btnToLogin = findViewById<TextView>(R.id.btn_to_login)
        val btnRegister = findViewById<TextView>(R.id.btn_register)
        val edtFullname = findViewById<EditText>(R.id.edt_fullname)
        val edtEmail = findViewById<EditText>(R.id.edt_email)
        val edtPassword = findViewById<EditText>(R.id.edt_password)
        btnToLogin.setOnClickListener{
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        btnRegister.setOnClickListener {
            val progressBar = findViewById<ProgressBar>(R.id.progressBar)
            progressBar.visibility = View.VISIBLE
            btnRegister.visibility = View.GONE
            progressBar.postDelayed({
                progressBar.visibility = View.GONE
            }, 10000)
            val fullname = edtFullname.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            // Periksa apakah EditText tidak kosong
            if (fullname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                Log.d("GATAU","BERHASIL")
                ApiClient.getInstance().postUser(
                    ApiUser(
                    name = fullname,
                    email = email,
                    password = password
                )
                ).enqueue(object : Callback<SingleResponse> {
                    override fun onResponse(call: Call<SingleResponse>, response: Response<SingleResponse>) {
                        if (response.isSuccessful) {
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Log.e("API", "Error: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<SingleResponse>, t: Throwable) {
                        Log.e("API", "Failure: ${t.message}")
                    }
                })

            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }



    }
}