package ru.mvrlrd.playlistmaker.mediateka.playlists.domain

import java.io.File

interface GetInternalFileUseCase {
    fun getInternalFile(imageName: String, albumName: String): File?
    fun getFileWhereToSavePicture(imageName: String, albumName: String): File
}