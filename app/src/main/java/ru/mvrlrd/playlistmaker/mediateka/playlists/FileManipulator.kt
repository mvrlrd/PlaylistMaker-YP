package ru.mvrlrd.playlistmaker.mediateka.playlists

import java.io.File

interface FileManipulator {
    fun getFile(imageName: String, albumName: String): File?
}