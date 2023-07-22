package ru.mvrlrd.playlistmaker.mediateka.playlists.domain

import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun getAllPlaylist(): Flow<List<PlaylistForAdapter>>
    suspend fun addPlaylist(playlistForAdapter: PlaylistForAdapter)

}