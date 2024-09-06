package com.angad.videoapp

import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.angad.videoapp.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    companion object{
        lateinit var player: ExoPlayer
        lateinit var playerList: ArrayList<Video>
        var position: Int = -1
        var repeat: Boolean = false
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        Hide the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER

//        Initialised the binding class
        binding = ActivityPlayerBinding.inflate(layoutInflater)

//        Set the playerActivity Theme
        setTheme(R.style.playerActivityTheme)
        setContentView(binding.root)

        //        For immersive mode i.e., full screen mode
        WindowCompat.setDecorFitsSystemWindows(window, false)
//        For hiding the buttons
        WindowInsetsControllerCompat(window, binding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

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
        binding.repeatBtn.setOnClickListener {
            if(repeat){
                repeat = false
                player.repeatMode = Player.REPEAT_MODE_OFF
                binding.repeatBtn.setImageResource(R.drawable.ic_repeat_off)
            }
            else{
                repeat = true
                player.repeatMode = Player.REPEAT_MODE_ONE
                binding.repeatBtn.setImageResource(R.drawable.ic_repeat_on)
            }
        }
    }

    private fun createPlayer(){
        try { player.release() }catch (e: Exception){ e.message}

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
        //    Set functionality of repeat button
        player.addListener(object: Player.Listener{
            override fun onPlaybackStateChanged(playbackState: Int){
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == Player.STATE_ENDED)
                    nextPrevVideo()
            }
        })
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
        if (!repeat){
            if (isIncrement){
                if(position == playerList.size - 1)
                    position = 0
                else
                    ++position
            }
            else{
                if(position == 0)
                    position = playerList.size -1
                else
                    --position
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}