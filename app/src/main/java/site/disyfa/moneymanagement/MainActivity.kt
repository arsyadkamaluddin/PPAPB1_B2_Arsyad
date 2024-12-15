package site.disyfa.moneymanagement

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import site.disyfa.moneymanagement.adapter.TransactionAdapter
import site.disyfa.moneymanagement.databinding.ActivityMainBinding
import site.disyfa.moneymanagement.model.Transaction
import site.disyfa.moneymanagement.ui.LoginActivity
import site.disyfa.moneymanagement.ui.RegisterActivity
import site.disyfa.moneymanagement.util.StringGenerator

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val navController = findNavController(R.id.nav_host_fragment)
        val bottomNavigationMenu = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationMenu.setupWithNavController(navController)
        val profileImage = findViewById<ImageView>(R.id.iv_profile)


        profileImage.setOnClickListener{
            navController.navigate(R.id.profileFragment)
        }
//        navController.navigate(R.id.categoryFragment)

        bottomNavigationMenu.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.categoryFragment -> {
                    navController.navigate(R.id.categoryFragment)
                    true
                }
                R.id.allFragment -> {
                    navController.navigate(R.id.allFragment)
                    true
                }
                R.id.addFragment -> {
                    navController.navigate(R.id.addFragment)
                    true
                }
                R.id.logoutFragment -> {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

    }

    fun updateBalance(amount: Int) {
        findViewById<TextView>(R.id.tv_nominal_balance).setText(StringGenerator.formatToRupiah(amount))
    }

}