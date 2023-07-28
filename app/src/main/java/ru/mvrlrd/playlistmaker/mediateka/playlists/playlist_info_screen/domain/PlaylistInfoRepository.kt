package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.TrackEntity
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter

interface PlaylistInfoRepository {
    fun getPlaylistWithSongs(id: Long): Flow<PlaylistInfo>

   suspend fun removeTrackFromPlaylist(trackId: Long, playlistId: Long): Flow<Int>

    fun getAllSongsForDebug(): Flow<List<TrackEntity>>

    suspend fun deletePlaylist(playlistId: Long)
   suspend fun getTrackListByDescDate(playlistId: Long): List<TrackForAdapter>


}