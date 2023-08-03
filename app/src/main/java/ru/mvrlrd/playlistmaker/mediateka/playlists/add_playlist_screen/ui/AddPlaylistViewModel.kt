package ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.ui

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.playlistmaker.mediateka.playlists.HandlePlaylistBaseViewModel
import ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.domain.AddPlaylistUseCase
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.GetInternalFileUseCase
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter

class AddPlaylistViewModel(
    private val interactor: AddPlaylistUseCase,
    useCase: GetInternalFileUseCase
) : HandlePlaylistBaseViewModel(useCase) {

    override fun handlePlaylist(playlist: PlaylistForAdapter) {
        viewModelScope.launch {
            interactor.addPlaylist(playlist)
        }
    }

    override fun createPlaylist(
        id: Long,
        name: String,
        description: String,
        imageUrl: String
    ): PlaylistForAdapter {
        return  PlaylistForAdapter(
            playlistId = 0,
            name = name,
            description = description,
            playlistImagePath = imageUrl
        )
    }
}