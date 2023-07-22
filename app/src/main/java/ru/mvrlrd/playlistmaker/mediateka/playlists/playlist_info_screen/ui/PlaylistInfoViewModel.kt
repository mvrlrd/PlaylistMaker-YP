package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain.PlaylistInfo
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain.PlaylistInfoInteractor
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter

class PlaylistInfoViewModel(private val interactor: PlaylistInfoInteractor, playlistId: Long): ViewModel() {
    private val _tracks = MutableLiveData<List<TrackForAdapter>>()
    val tracks : LiveData<List<TrackForAdapter>> get()= _tracks

    private val _ids = MutableLiveData<List<Long>>()
     val ids : LiveData<List<Long>> get( ) = _ids

     private val _screenState  = MutableLiveData<PlaylistInfoScreenState>()
     val screenState : LiveData<PlaylistInfoScreenState>  get()= _screenState

    val idssss = interactor.getFavsIds()


    init{
          viewModelScope.launch(){
               interactor.getPlaylist(playlistId).flowOn(Dispatchers.IO)
                    .collect(){
                        _tracks.postValue(it.songs)
                        _screenState.postValue(PlaylistInfoScreenState.InitialState(it))
                    }
          }
     }

    fun onResume(){
        viewModelScope.launch {
            interactor.getFavsIds().collect(){
                _ids.postValue(it)
            }
        }
    }

    fun isFavorite(id: Long): Boolean{
        return ids.value?.contains(id) ?: false
    }
}