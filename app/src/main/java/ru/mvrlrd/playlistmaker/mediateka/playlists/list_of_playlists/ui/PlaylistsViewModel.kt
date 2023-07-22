package ru.mvrlrd.playlistmaker.mediateka.playlists.list_of_playlists.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import ru.mvrlrd.playlistmaker.mediateka.playlists.list_of_playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.mediateka.playlists.list_of_playlists.domain.PlaylistInteractor

class PlaylistsViewModel(interactor: PlaylistInteractor) : ViewModel() {
    val playlists: LiveData<List<PlaylistForAdapter>> =
            interactor.getAllPlaylist().asLiveData()
}