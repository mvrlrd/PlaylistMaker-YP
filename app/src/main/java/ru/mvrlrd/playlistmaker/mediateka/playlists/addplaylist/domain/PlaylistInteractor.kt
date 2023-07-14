package ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain

import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun getAllPlaylist(): Flow<List<AdapterPlaylist>>
    suspend fun addPlaylist(adapterPlaylist: AdapterPlaylist)
}