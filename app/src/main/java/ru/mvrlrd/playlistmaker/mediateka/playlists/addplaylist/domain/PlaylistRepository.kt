package ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain

import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getAllPlaylist(): Flow<List<PlaylistForAdapter>>
    suspend fun insertPlaylist(playlistForAdapter: PlaylistForAdapter)
}