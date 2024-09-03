package com.angad.videoapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.angad.videoapp.databinding.FoldersViewBinding

class FoldersAdapter(private val context: Context, private var foldersList: ArrayList<Folder>): RecyclerView.Adapter<FoldersAdapter.MyFolderHolder>() {

//    This class hold the view of folders_view.xml file
    class MyFolderHolder(binding: FoldersViewBinding): RecyclerView.ViewHolder(binding.root) {
        val folderName = binding.folderNameFV
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):  MyFolderHolder {
        return MyFolderHolder(FoldersViewBinding.inflate(LayoutInflater.from(context), parent, false))
     }

    override fun onBindViewHolder(holder:  MyFolderHolder, position: Int) {
        holder.folderName.text = foldersList[position].folderName
    }

    override fun getItemCount(): Int {
        return foldersList.size
    }


}