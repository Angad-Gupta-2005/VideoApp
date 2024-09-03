package com.angad.videoapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.angad.videoapp.databinding.FoldersViewBinding

class FoldersAdapter(private val context: Context, private var foldersList: ArrayList<Folder>): RecyclerView.Adapter<FoldersAdapter.MyFolderHolder>() {

//    This class hold the view of folders_view.xml file
    class MyFolderHolder(binding: FoldersViewBinding): RecyclerView.ViewHolder(binding.root) {
        val folderName = binding.folderNameFV
        val root = binding.root
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):  MyFolderHolder {
        return MyFolderHolder(FoldersViewBinding.inflate(LayoutInflater.from(context), parent, false))
     }

    override fun onBindViewHolder(holder:  MyFolderHolder, position: Int) {
        holder.folderName.text = foldersList[position].folderName
        holder.root.setOnClickListener {
//            On click on folder icon we moved on the FolderActivity
            val intent = Intent(context, FoldersActivity::class.java)
//            With moved we pass some data
            intent.putExtra("position", position)
//            Starting the activity
            ContextCompat.startActivity(context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return foldersList.size
    }


}