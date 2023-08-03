package ru.mvrlrd.playlistmaker.mediateka.playlists.domain

import java.io.File

interface InternalStorageRepository {
    fun getFile(imageName: String, albumName: String): File?
    fun getFileWhereToSavePicture(imageName: String, albumName: String): File
}