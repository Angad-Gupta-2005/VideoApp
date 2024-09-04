package com.angad.videoapp

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.angad.videoapp.databinding.ActivityFoldersBinding
import java.io.File

class FoldersActivity : AppCompatActivity() {

    companion object{
        lateinit var currentFolderVideos: ArrayList<Video>
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFoldersBinding.inflate(layoutInflater)
         setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val position = intent.getIntExtra("position", 0)
//        Changing the title/name of Action Bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = MainActivity.folderList[position].folderName

//        Calling the getAllVideos
        currentFolderVideos = getAllVideos(MainActivity.folderList[position].id)

//        Initialized the RecyclerView
        binding.recyclerViewFA.setHasFixedSize(true)
        binding.recyclerViewFA.setItemViewCacheSize(8)
        binding.recyclerViewFA.layoutManager = LinearLayoutManager(this@FoldersActivity)
        binding.recyclerViewFA.adapter = VideoAdapter(this@FoldersActivity, currentFolderVideos, isFolder = true)
        binding.totalVideosFA.text = "Total Videos: ${currentFolderVideos.size}"

    }

//    Functionality of back button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }


    //    copy from main activity
    @SuppressLint("Recycle", "Range")
    private fun getAllVideos(folderId: String): ArrayList<Video>{
        val tempList = ArrayList<Video>()
        val selection = MediaStore.Video.Media.BUCKET_ID + " like? "

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
            selection,
            arrayOf(folderId),
            MediaStore.Video.Media.DATE_ADDED + " DESC "
        )

        if (cursor != null){
            if (cursor.moveToNext()){
                do {
                    val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                    val sizeC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
                    val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                    val folderC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
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