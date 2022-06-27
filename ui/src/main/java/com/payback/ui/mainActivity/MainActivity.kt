package com.payback.ui.mainActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.payback.ui.databinding.ActivityMainBinding
import com.payback.ui.utilities.changeStatusBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        changeStatusBar(true)
    }

}