package ru.mvrlrd.playlistmaker.mediateka.playlists.edit_playlist.domain

import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter

interface UpdatePlaylistRepository {
    suspend fun updatePlaylist(playlist: PlaylistForAdapter)
}