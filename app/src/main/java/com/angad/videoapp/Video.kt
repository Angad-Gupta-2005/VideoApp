package com.angad.videoapp

import android.net.Uri

data class Video(
    val id: String,
    val title: String,
    val duration: Long = 0,
    val folderName: String,
    val size: String,
    val path: String,
    val artUri: Uri
)

// Data class to store the data of folder

data class Folder(
    val id: String,
    val folderName: String
)