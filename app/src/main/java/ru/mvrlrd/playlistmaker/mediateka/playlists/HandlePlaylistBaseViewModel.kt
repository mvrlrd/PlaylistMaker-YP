package ru.mvrlrd.playlistmaker.mediateka.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.ui.AddPlaylistScreenState
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.GetInternalFileUseCase
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter


abstract class HandlePlaylistBaseViewModel(useCase: GetInternalFileUseCase): FileOperatingViewModel(useCase) {
    private val _screenState = MutableLiveData<AddPlaylistScreenState>()
    val screenState: LiveData<AddPlaylistScreenState> = _screenState


    abstract fun handlePlaylist(playlist: PlaylistForAdapter)

    fun changeSubmitButtonStatus(text: String?){
        _screenState.value = AddPlaylistScreenState.CreateButtonHandler(text.isNullOrEmpty())
    }

     fun ifDataUnsaved(name: String?, description: String?, isPictureChosen: Boolean ): Boolean {
        return ( isPictureChosen
                || !name.isNullOrEmpty()
                || !description.isNullOrEmpty())
    }

   abstract fun createPlaylist(id: Long, name: String, description: String, imageUrl: String): PlaylistForAdapter
}
