package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain.PlaylistInfo
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain.PlaylistInfoInteractor

class PlaylistInfoViewModel(private val interactor: PlaylistInfoInteractor,  playlistId: Long) :
    ViewModel() {
    private val _screenState = MutableLiveData<PlaylistInfoScreenState>()
    val screenState: LiveData<PlaylistInfoScreenState> get() = _screenState

    val playlistInfo = interactor.getPlaylist(playlistId)

    val allSongsDebugging = interactor.getAllSongsForDebug()
    fun changeState(playlistInfo: PlaylistInfo) {
        _screenState.postValue(PlaylistInfoScreenState.InitialState(playlistInfo))
    }

    fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long){
        viewModelScope.launch {
            interactor.deleteTrackFromPlaylist(trackId, playlistId).collect(){
                Log.d("PlaylistInfoViewModel", "deleted $it  items")
            }
        }
    }
}

