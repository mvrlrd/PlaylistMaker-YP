package ru.mvrlrd.playlistmaker.mediateka.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistInteractor

class PlaylistsViewModel(private val interactor: PlaylistInteractor) : ViewModel() {
    val playlists: LiveData<List<PlaylistForAdapter>> =
            interactor.getAllPlaylist().asLiveData()
}