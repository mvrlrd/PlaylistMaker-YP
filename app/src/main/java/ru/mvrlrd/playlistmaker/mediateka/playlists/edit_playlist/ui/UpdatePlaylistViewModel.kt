package ru.mvrlrd.playlistmaker.mediateka.playlists.edit_playlist.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.playlistmaker.mediateka.playlists.HandlePlaylistBaseViewModel
import ru.mvrlrd.playlistmaker.mediateka.playlists.PlaylistEditingBaseFragment
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.mediateka.playlists.edit_playlist.domain.UpdatePlaylistUseCase
import ru.mvrlrd.playlistmaker.tools.generateImageNameForStorage

open class UpdatePlaylistViewModel(private val updatePlaylistUseCase: UpdatePlaylistUseCase): HandlePlaylistBaseViewModel() {

    override fun handlePlaylist(playlist: PlaylistForAdapter) {
        viewModelScope.launch {
            Log.d(PlaylistEditingBaseFragment.TAG, " playlist = ${playlist} updated in bd")
            updatePlaylistUseCase.updatePlaylist(playlist)
        }
    }

    override fun createPlaylist(
        id: Long,
        name: String,
        description: String,
        imageUrl: String
    ): PlaylistForAdapter {
        return  PlaylistForAdapter(
            playlistId = id,
            name = name,
            description = description,
            playlistImagePath = imageUrl
        )
    }

    override fun getImagePath(isPictureChosen: Boolean, path: String?): String {
        return if (isPictureChosen){
            generateImageNameForStorage()
        }else{
            path?:""
        }
    }
}