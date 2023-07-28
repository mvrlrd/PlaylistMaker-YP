package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.TrackEntity
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter

interface PlaylistInfoRepository {
    fun getPlaylistInfo(id: Long): Flow<PlaylistInfo>
    suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long): Flow<Int>
    suspend fun deletePlaylist(playlistId: Long)
    suspend fun getTracksByDescDate(playlistId: Long): List<TrackForAdapter>


    fun getAllTacksForDebugging(): Flow<List<TrackEntity>>

}