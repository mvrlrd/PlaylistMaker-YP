package ru.mvrlrd.playlistmaker

import java.io.File

interface FileManipulator {
    fun getFile(imageName: String, albumName: String): File?
}