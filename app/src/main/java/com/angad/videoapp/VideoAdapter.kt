package com.angad.videoapp

import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.angad.videoapp.databinding.VideoViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class VideoAdapter(private val context: Context, private var videoList: ArrayList<Video>, private val isFolder: Boolean = false) :
    RecyclerView.Adapter<VideoAdapter.MyHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(VideoViewBinding.inflate(LayoutInflater.from(context), parent, false))

    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.title.text = videoList[position].title
        holder.folder.text = videoList[position].folderName
        holder.duration.text = DateUtils.formatElapsedTime(videoList[position].duration / 1000)

//        Functionality to play the video by ExoPLayer library
        Glide.with(context).asBitmap().load(videoList[position].artUri)
            .apply(RequestOptions().placeholder(R.mipmap.ic_video_player).centerCrop())
            .into(holder.image)

        holder.root.setOnClickListener {
            when{
                isFolder->{
                    sendIntent(pos = position, ref = "FolderActivity")
                }
                else ->{
                    sendIntent(pos = position, ref = "AllVideos")
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    /*  Imp i.e., this function of code move to the PlayerActivity file
when click on any video   */
    private fun sendIntent(pos: Int, ref: String){
        PlayerActivity.position = pos
        val intent = Intent(context, PlayerActivity::class.java)
        intent.putExtra("class",ref)
        ContextCompat.startActivity(context, intent, null)
    }

    //  This class hold the view of video_view.xml file
    class MyHolder(binding: VideoViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.videoName
        val folder = binding.folderName
        val duration = binding.duration
        val image = binding.videoImg
        val root = binding.root
    }

}
