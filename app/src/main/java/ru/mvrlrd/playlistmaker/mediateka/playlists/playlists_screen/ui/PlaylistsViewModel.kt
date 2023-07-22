package ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.domain.PlaylistInteractor

class PlaylistsViewModel(interactor: PlaylistInteractor) : ViewModel() {
    val playlists: LiveData<List<PlaylistForAdapter>> =
            interactor.getAllPlaylist().asLiveData()
}