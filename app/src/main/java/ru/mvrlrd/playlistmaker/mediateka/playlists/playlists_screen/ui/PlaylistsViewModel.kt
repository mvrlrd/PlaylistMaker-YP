package ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.ui

import androidx.lifecycle.ViewModel
import ru.mvrlrd.playlistmaker.FileOperatingViewModel
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.GetInternalFileUseCase
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.domain.PlaylistInteractor
import java.io.File

class PlaylistsViewModel(useCase: PlaylistInteractor, private val fileUseCase: GetInternalFileUseCase) : ViewModel(), FileOperatingViewModel {
    val playlists = useCase.getAllPlaylist()

    override fun getFile(imageName: String, albumName: String): File? {
        return fileUseCase.getInternalFile(imageName, albumName)
    }
}