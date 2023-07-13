package ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddPlaylistViewModel: ViewModel() {
    private val _screenState = MutableLiveData<AddPlaylistScreenState>()
    val screenState: LiveData<AddPlaylistScreenState> = _screenState

    fun clearNameFieldText(){
        _screenState.value = AddPlaylistScreenState.ClearNamFieldText
    }

    fun clearDescriptionFieldText(){
        _screenState.value = AddPlaylistScreenState.ClearDescriptionFieldText
    }



    fun initEditTextFields(){
        _screenState.value = AddPlaylistScreenState.InitEditTextFields
    }

}