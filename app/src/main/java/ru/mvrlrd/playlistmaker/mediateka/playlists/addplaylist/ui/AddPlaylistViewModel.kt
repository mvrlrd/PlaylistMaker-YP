package ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.AdapterPlaylist
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistInteractor

class AddPlaylistViewModel(private val interactor: PlaylistInteractor): ViewModel() {
    private val _screenState = MutableLiveData<AddPlaylistScreenState>()
    val screenState: LiveData<AddPlaylistScreenState> = _screenState
    val playlists: LiveData<List<AdapterPlaylist>> =
        interactor.getAllPlaylist().asLiveData()
    fun addPlaylist(adapterPlaylist: AdapterPlaylist){
        viewModelScope.launch {
            interactor.addPlaylist(adapterPlaylist)
        }
    }



    fun clearNameFieldText(){
        _screenState.value = AddPlaylistScreenState.ClearNamFieldText
    }

    fun clearDescriptionFieldText(){
        _screenState.value = AddPlaylistScreenState.ClearDescriptionFieldText
    }

    fun initEditTextFields(){
        _screenState.value = AddPlaylistScreenState.InitEditTextFields
    }

    fun generateImageNameForStorage(): String{
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..5)
            .map { allowedChars.random() }
            .joinToString("")
            .plus(".jpg")
    }

}