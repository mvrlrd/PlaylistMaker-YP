package ru.mvrlrd.playlistmaker.mediateka.playlists.edit_playlist.ui

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.playlistmaker.mediateka.playlists.HandlePlaylistBaseViewModel
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.mediateka.playlists.edit_playlist.domain.UpdatePlaylistUseCase

open class UpdatePlaylistViewModel(private val updatePlaylistUseCase: UpdatePlaylistUseCase): HandlePlaylistBaseViewModel() {

    override fun handlePlaylist(playlist: PlaylistForAdapter) {
        viewModelScope.launch {
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
}