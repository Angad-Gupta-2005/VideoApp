package com.angad.videoapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.angad.videoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        initialised the view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
//        enableEdgeToEdge()
        setContentView(binding.root)

//        calling the function to set fragment and setting the VideoFragment as a default fragment
        setFragment(VideosFragment())

//        setting the custom theme
        setTheme(R.style.coolPinkNav)  //not working properly

//        binding.helloAn.text = "Angad Gupta"

        binding.bottomNav.setOnItemSelectedListener {
             when(it.itemId){
                 R.id.videoView -> setFragment(VideosFragment())
                 R.id.folderView -> setFragment(FoldersFragment())
             }
            return@setOnItemSelectedListener true
        }
    }

//    Function to set the fragment
    private fun setFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentFL, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

}