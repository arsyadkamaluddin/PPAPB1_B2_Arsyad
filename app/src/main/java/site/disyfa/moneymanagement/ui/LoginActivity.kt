package site.disyfa.moneymanagement.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import site.disyfa.moneymanagement.MainActivity
import site.disyfa.moneymanagement.R
import site.disyfa.moneymanagement.localdb.AppRoomDatabase
import site.disyfa.moneymanagement.localdb.UserDao
import site.disyfa.moneymanagement.util.DBInitializer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LoginActivity : AppCompatActivity() {
    private lateinit var mUserDao: UserDao
    private lateinit var executorService: ExecutorService
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DBInitializer(application).syncDatabase()
        executorService = Executors.newSingleThreadExecutor()
        val db = AppRoomDatabase.getDatabase(this)
        mUserDao = db!!.userDao()!!
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        val btnToRegister = findViewById<TextView>(R.id.btn_to_register)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        btnToRegister.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        btnLogin.setOnClickListener {
            val progressBar = findViewById<ProgressBar>(R.id.progressBar)
            progressBar.visibility = View.VISIBLE
            btnLogin.visibility = View.GONE
            progressBar.postDelayed({
                progressBar.visibility = View.GONE
                btnLogin.visibility = View.VISIBLE
            }, 1000)
            val edtEmail: EditText = findViewById(R.id.edt_email)
            val edtPassword: EditText = findViewById(R.id.edt_password)
            validateUser(edtEmail.text.toString(), edtPassword.text.toString()) { isValid ->
                if (isValid) {
                    mUserDao.getUserByEmail(edtEmail.text.toString()).observe(this, Observer { user ->
                        val editor = sharedPreferences.edit()
                        editor.putString("user_id", user.id)
                        editor.putString("name", user.name)
                        editor.putString("email", user.email)
                        editor.apply()
                    })
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
    fun validateUser(email: String, passwordEdt: String, callback: (Boolean) -> Unit) {
        mUserDao.getPassword(email).observe(this, Observer { password ->
            if (password != null) {
                callback(password == passwordEdt)
            } else {
                callback(false)
            }
        })
    }


}