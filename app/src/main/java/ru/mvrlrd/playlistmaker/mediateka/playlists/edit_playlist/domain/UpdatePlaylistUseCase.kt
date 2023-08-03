package ru.mvrlrd.playlistmaker.mediateka.playlists.edit_playlist.domain

import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter

interface UpdatePlaylistUseCase {
    suspend fun updatePlaylist(playlist: PlaylistForAdapter)
}