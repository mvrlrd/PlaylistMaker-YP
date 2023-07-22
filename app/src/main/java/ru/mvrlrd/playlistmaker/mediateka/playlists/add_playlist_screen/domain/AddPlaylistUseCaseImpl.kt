package ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.domain

import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter

class AddPlaylistUseCaseImpl(private val repository: AddPlaylistRepository): AddPlaylistUseCase {
    override suspend fun addPlaylist(playlist: PlaylistForAdapter) {
        repository.insertPlaylist(playlist)
    }
}