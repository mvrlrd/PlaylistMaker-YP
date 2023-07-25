package ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.ui

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.playlistmaker.mediateka.playlists.HandlePlaylistBaseViewModel
import ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.domain.AddPlaylistUseCase
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter

class AddPlaylistViewModel(private val interactor: AddPlaylistUseCase) : HandlePlaylistBaseViewModel() {
    override fun handlePlaylist(playlist: PlaylistForAdapter) {
        viewModelScope.launch {
            interactor.addPlaylist(playlist)
        }
    }
}