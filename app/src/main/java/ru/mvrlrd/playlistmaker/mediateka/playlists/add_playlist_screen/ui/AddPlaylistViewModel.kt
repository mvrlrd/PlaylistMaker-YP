package ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.domain.AddPlaylistUseCase
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter

class AddPlaylistViewModel(private val interactor: AddPlaylistUseCase) : ViewModel() {
    private val _screenState = MutableLiveData<AddPlaylistScreenState>()
    val screenState: LiveData<AddPlaylistScreenState> = _screenState

    fun addPlaylist(playlistForAdapter: PlaylistForAdapter) {
        viewModelScope.launch {
            interactor.addPlaylist(playlistForAdapter)
        }
    }

    fun handleCreateButtonVisibility(text: String?){
        _screenState.value = AddPlaylistScreenState.CreateButtonHandler(text.isNullOrEmpty())
    }
    fun generateImageNameForStorage(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..5)
            .map { allowedChars.random() }
            .joinToString("")
            .plus(IMAGE_TYPE)
    }
    companion object{
        const val IMAGE_TYPE = ".jpg"
    }
}