package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain.PlaylistInfo
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain.PlaylistInfoInteractor

class PlaylistInfoViewModel(private val interactor: PlaylistInfoInteractor, playlistId: Long): ViewModel() {
     val playlist = interactor.getPlaylist(playlistId).asLiveData()

}