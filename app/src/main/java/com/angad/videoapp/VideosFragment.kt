package com.angad.videoapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.angad.videoapp.databinding.FragmentVideosBinding

class VideosFragment : Fragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_videos, container, false)

//        Initialised the RecyclerView
        val binding = FragmentVideosBinding.bind(view)

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.setItemViewCacheSize(10)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = VideoAdapter(requireContext(), MainActivity.videoList)
        binding.totalVideos.text = "Total Videos: ${MainActivity.videoList.size}"
        return view
    }
    
}