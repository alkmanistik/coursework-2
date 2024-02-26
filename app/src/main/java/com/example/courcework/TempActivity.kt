package com.example.courcework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class TempActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp)
    }

    override fun onResume() {
        super.onResume()
        onBackPressed()
    }
}