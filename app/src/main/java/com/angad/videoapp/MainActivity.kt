package com.angad.videoapp

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_AUDIO
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.angad.videoapp.databinding.ActivityMainBinding
import java.io.File
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
//    for navigation drawer
    private lateinit var toggle: ActionBarDrawerToggle

//    For fetch data for media storage
    companion object{
        lateinit var videoList: ArrayList<Video>
        lateinit var folderList: ArrayList<Folder>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        initialised the view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
//        enableEdgeToEdge()
        setContentView(binding.root)



//        For Nav Drawer
        toggle = ActionBarDrawerToggle(this, binding.root, R.string.open, R.string.close)
        binding.root.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        request permissions of Media storage
        if (requestRuntimePermission()){
//            Initialized the folderList
            folderList = ArrayList()
            videoList = getAllVideos()
//        calling the function to set fragment and setting the VideoFragment as a default fragment
            setFragment(VideosFragment())
        }


//        setting the custom theme
        setTheme(R.style.coolPinkNav)  //not working properly

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.videoView -> setFragment(VideosFragment())
                R.id.folderView -> setFragment(FoldersFragment())
            }
            return@setOnItemSelectedListener true
        }

//    Adding listener to the item of the drawer layout
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.feedbackNav -> Toast.makeText(this, "Feedback Clicked", Toast.LENGTH_SHORT).show()
                R.id.themesNav -> Toast.makeText(this, "Theme Clicked", Toast.LENGTH_SHORT).show()
                R.id.sortOrderNav -> Toast.makeText(this, "Sort Order Clicked", Toast.LENGTH_SHORT).show()
                R.id.aboutNav -> Toast.makeText(this, "About Clicked", Toast.LENGTH_SHORT).show()
                R.id.exitNav -> exitProcess(1)
            }
            return@setNavigationItemSelectedListener true
        }

    }

    //    Function to set the fragment
    private fun setFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentFL, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }


    private fun requestRuntimePermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            For android 13 or above
            if (checkSelfPermission(READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED){
                return true
            }

            ActivityCompat.requestPermissions(this, arrayOf(READ_MEDIA_VIDEO, READ_MEDIA_AUDIO), 13)
            return false
        } else {
//            For version below android 13
            if (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true
            }

            ActivityCompat.requestPermissions(
                this, arrayOf(
                    WRITE_EXTERNAL_STORAGE,
                    READ_EXTERNAL_STORAGE
                ), 13
            )
            return false
        }
     }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 13) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Fixing the bug when first time user give permission
                folderList = ArrayList()
                videoList = getAllVideos()
                setFragment(VideosFragment())
                Toast.makeText(this, "Permission Granted ", Toast.LENGTH_SHORT).show()
            } else {
//                ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE), 13)
                Toast.makeText(
                    this,
                    "Permission Denied!!, Open the setting and give the permission",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

//    For Nav Drawer
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

//    create the cursor object and access the data
    @SuppressLint("Recycle", "Range")
    private fun getAllVideos(): ArrayList<Video>{
        val tempList = ArrayList<Video>()
        val tempFolderList = ArrayList<String>()

        val projection = arrayOf(
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DURATION
            )
//    Fetch the mention data
        val cursor = this.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Video.Media.DATE_ADDED + " DESC "
        )

        if (cursor != null){
            if (cursor.moveToNext()){
                do {
                    val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                    val sizeC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
                    val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                    val folderC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                    val folderIdC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID))
                    val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                    val durationC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)).toLong()

                    try {
                        val file = File(pathC)
                        val artUriC = Uri.fromFile(file)
                        val video = Video(
                            title = titleC,
                            id = idC,
                            folderName = folderC,
                            duration = durationC,
                            size = sizeC,
                            path = pathC,
                            artUri = artUriC
                            )
                        if (file.exists()){
                            tempList.add(video)
                        }

//                        For adding folders
                        if (!tempFolderList.contains(folderC)){
                            tempFolderList.add(folderC)
                            folderList.add(Folder(id = folderIdC, folderName = folderC))
                        }

                    }catch (e: Exception){
                        Toast.makeText(this, "Error fetching video: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }while (cursor.moveToNext())
            }
        }
        cursor?.close()
        return tempList
    }
}