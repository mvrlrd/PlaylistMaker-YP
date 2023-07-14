package ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain

import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getAllPlaylist(): Flow<List<AdapterPlaylist>>
    suspend fun insertPlaylist(adapterPlaylist: AdapterPlaylist)
}