package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain.PlaylistInfo
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain.PlaylistInfoInteractor
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter

class PlaylistInfoViewModel(interactor: PlaylistInfoInteractor, playlistId: Long) :
    ViewModel() {
    private val _screenState = MutableLiveData<PlaylistInfoScreenState>()
    val screenState: LiveData<PlaylistInfoScreenState> get() = _screenState

    val playlistInfo = interactor.getPlaylist(playlistId)
    fun changeState(playlistInfo: PlaylistInfo) {
        _screenState.postValue(PlaylistInfoScreenState.InitialState(playlistInfo))
    }
}