package com.angad.videoapp

 import android.content.Context
 import android.view.LayoutInflater
 import android.view.ViewGroup
 import androidx.recyclerview.widget.RecyclerView
 import com.angad.videoapp.databinding.VideoViewBinding

class VideoAdapter(private val context: Context, private var videoList: ArrayList<String>):
    RecyclerView.Adapter<VideoAdapter.MyHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(VideoViewBinding.inflate(LayoutInflater.from(context), parent, false))

    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.title.text = videoList[position]
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

//  This class hold the view of video_view.xml file
    class MyHolder(binding: VideoViewBinding): RecyclerView.ViewHolder(binding.root) {
        val title = binding.videoName

    }

}
