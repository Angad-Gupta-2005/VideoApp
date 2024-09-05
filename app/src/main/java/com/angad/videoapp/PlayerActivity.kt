package com.angad.videoapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.angad.videoapp.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    companion object{
        lateinit var player: ExoPlayer
        lateinit var playerList: ArrayList<Video>
        var position: Int = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initializedLayout()
//        calling the initializeBinding() method
        initializeBinding()
    }

    private fun initializedLayout(){
        when(intent.getStringExtra("class")){
            "AllVideos" ->{
                playerList = ArrayList()
                playerList.addAll(MainActivity.videoList)
                createPlayer()
            }
            "FolderActivity" -> {
                playerList = ArrayList()
                playerList.addAll(FoldersActivity.currentFolderVideos)
                createPlayer()
            }
        }

    }

//    Setting all button functionality
    private fun initializeBinding(){
        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.playPauseBtn.setOnClickListener {
            if (player.isPlaying){
                pauseVideo()
            }
            else{
                playVideo()
            }
        }

//        next and previous button functionality
        binding.nextBtn.setOnClickListener { nextPrevVideo() }
        binding.prevBtn.setOnClickListener { nextPrevVideo(isNext = false) }
    }

    private fun createPlayer(){
        binding.videoTitle.text = playerList[position].title
        binding.videoTitle.isSelected = true
//        Creating an object of player
        player =  ExoPlayer.Builder(this).build()
//        Set up player view
        binding.playerView.player = player
        val mediaItem = MediaItem.fromUri(playerList[position].artUri)
        player.setMediaItem(mediaItem)
        player.prepare()
        playVideo()
    }

//    Functionality of play and pause of video
    private fun playVideo(){
        binding.playPauseBtn.setImageResource(R.drawable.ic_pause)
        player.play()
    }

    private fun pauseVideo(){
        binding.playPauseBtn.setImageResource(R.drawable.ic_play)
        player.pause()
    }

//    Function to perform next and previous video
    private fun nextPrevVideo(isNext: Boolean = true){
        if(isNext){
            player.release()
            setPosition()
        }
        else{
            player.release()
            setPosition(isIncrement = false)
        }
//    Calling the createPlayer() function to play the video
        createPlayer()
    }

    private fun setPosition(isIncrement: Boolean = true){
        if (isIncrement){
            if(position == playerList.size - 1)
                position = 0
            else
                ++position
        }
        else{
            if (position == 0)
                position = playerList.size -1
            else
                --position
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}