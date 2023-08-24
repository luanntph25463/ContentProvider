package com.example.contentprovider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.my_draw_layout

class MainActivity : AppCompatActivity() {
    lateinit var my_draw_layout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        my_draw_layout = findViewById<DrawerLayout>(R.id.my_draw_layout)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, my_draw_layout, R.string.nav_open, R.string.nav_close)
        my_draw_layout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView: NavigationView = findViewById(R.id.navigation)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_account -> {
                    // Handle click on Item 1
                    // Open a new file or perform specific action
                    val fragment = Fragment1()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit()
                    true
                }

                R.id.home -> {
                    // Handle click on Item 1
                    // Open a new file or perform specific action
                    val fragment = Fragment2()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit()
                    true
                }
                R.id.info -> {
                    // Handle click on Item 1
                    // Open a new file or perform specific action
                    val fragment = infomation_Contact()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit()
                    true
                }
                // Handle other menu items as needed
                else -> {
                    val fragment = Fragment2()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit()
                    true
                }
            }
        }
    }
    override  fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

}