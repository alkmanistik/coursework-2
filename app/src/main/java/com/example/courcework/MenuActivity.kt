package com.example.courcework

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.example.courcework.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private var onCatalog: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.navigation_catalog -> replaceFragment(Catalog())
                R.id.navigation_basket -> replaceFragment(Basket())
                R.id.navigation_profile -> replaceFragment(Profile())
                R.id.navigation_settings -> replaceFragment(Settings())
                else -> {
                }
            }
            true
        }

        binding.bottomNavigationView.post{
            when (intent.getStringExtra("page")){
                "Profile" -> binding.bottomNavigationView.selectedItemId = R.id.navigation_profile
                else -> binding.bottomNavigationView.selectedItemId = R.id.navigation_catalog
            }
        }

    }
    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val classFragment = fragment.javaClass.kotlin.simpleName
        fragmentTransaction.replace(R.id.frameLayoutMenu,fragment)
        fragmentTransaction.commit()
        onCatalog = (classFragment == "Catalog")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}

