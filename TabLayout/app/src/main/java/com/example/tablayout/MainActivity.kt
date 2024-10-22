package com.example.tablayout

import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.tablayout.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.example.tablayout.OnFormListener

class MainActivity : AppCompatActivity(), OnFormListener {
    private lateinit var viewPager: ViewPager2
    companion object{
        private val TAB_TITLES = intArrayOf(
            R.string.login,
            R.string.register
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionPagerAdapter = PagerAdapter(this)
        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tab_layout)
        TabLayoutMediator(tabs,viewPager){tab,position->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    override fun onRegister() {
        viewPager.currentItem = 0
    }
    override fun onLogin() {
        viewPager.currentItem = 0
    }override fun toRegister() {
        viewPager.currentItem = 1
    }
}