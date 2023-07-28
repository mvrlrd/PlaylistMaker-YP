package ru.mvrlrd.playlistmaker

import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.GetInternalFileUseCase
import java.io.File

interface FileOperatingViewModel {
    fun getFile(imageName: String, albumName: String): File?
}