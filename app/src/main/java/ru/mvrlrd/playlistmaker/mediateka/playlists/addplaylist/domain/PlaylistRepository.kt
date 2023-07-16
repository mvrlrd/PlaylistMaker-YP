package ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain

import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun insertPlaylist(playlistForAdapter: PlaylistForAdapter)
    fun getAllPlaylistsWithSongs(): Flow<List<PlaylistForAdapter>>
}