package com.angad.videoapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angad.videoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        initialised the view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
//        enableEdgeToEdge()
        setContentView(binding.root)

        binding.helloAn.text = "Angad Gupta"
    }
}