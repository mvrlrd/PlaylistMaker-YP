package ru.mvrlrd.playlistmaker.mediateka.playlists.domain

import android.net.Uri
import java.io.File

interface InternalStorageRepository {
    fun getFile(imageName: String, albumName: String): File?
    fun saveImageToInternalStorage(uri: Uri?, imageName: String, albumName: String)

}