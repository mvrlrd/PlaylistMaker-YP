package ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.domain

import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter

interface AddPlaylistRepository {
    suspend fun insertPlaylist(playlistForAdapter: PlaylistForAdapter)
}