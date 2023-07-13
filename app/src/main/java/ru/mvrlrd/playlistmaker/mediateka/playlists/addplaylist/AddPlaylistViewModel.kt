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

    fun generateImageNameForStorage(): String{
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..5)
            .map { allowedChars.random() }
            .joinToString("")
            .plus(".jpg")
    }

}