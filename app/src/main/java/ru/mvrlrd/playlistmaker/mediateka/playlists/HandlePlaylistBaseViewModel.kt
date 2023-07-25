package ru.mvrlrd.playlistmaker.mediateka.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.ui.AddPlaylistScreenState
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter

abstract class HandlePlaylistBaseViewModel: ViewModel() {
    private val _screenState = MutableLiveData<AddPlaylistScreenState>()
    val screenState: LiveData<AddPlaylistScreenState> = _screenState


    abstract fun handlePlaylist(playlist: PlaylistForAdapter)

    fun changeSubmitButtonStatus(text: String?){
        _screenState.value = AddPlaylistScreenState.CreateButtonHandler(text.isNullOrEmpty())
    }

    private fun generateImageNameForStorage(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..5)
            .map { allowedChars.random() }
            .joinToString("")
            .plus(IMAGE_TYPE)
    }

     fun getImagePath(isPictureChosen: Boolean): String{
        return if (isPictureChosen){
            generateImageNameForStorage()
        }else{
            ""
        }
    }

     fun ifDataUnsaved(name: String?, description: String?, isPictureChosen: Boolean ): Boolean {
        return ( isPictureChosen
                || !name.isNullOrEmpty()
                || !description.isNullOrEmpty())
    }

   abstract fun createPlaylist(id: Long, name: String, description: String, imageUrl: String): PlaylistForAdapter
    companion object{
        const val IMAGE_TYPE = ".jpg"
    }
}
