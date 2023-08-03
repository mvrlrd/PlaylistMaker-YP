package ru.mvrlrd.playlistmaker.mediateka.playlists.edit_playlist.domain

import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter

class UpdatePlaylistUseCaseImpl(private val repository: UpdatePlaylistRepository): UpdatePlaylistUseCase {
    override suspend fun updatePlaylist(playlist: PlaylistForAdapter) {
        repository.updatePlaylist(playlist)
    }
}