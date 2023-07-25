package ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.ui

import androidx.lifecycle.ViewModel
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.domain.PlaylistInteractor

class PlaylistsViewModel(useCase: PlaylistInteractor) : ViewModel() {
    val playlists = useCase.getAllPlaylist()
}