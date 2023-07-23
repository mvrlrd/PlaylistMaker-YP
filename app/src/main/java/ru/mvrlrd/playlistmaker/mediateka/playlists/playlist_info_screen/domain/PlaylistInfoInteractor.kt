package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.Song

interface PlaylistInfoInteractor {
    fun getPlaylist(id: Long): Flow<PlaylistInfo>

   suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long):Flow<Int>

    fun getAllSongsForDebug(): Flow<List<Song>>

    suspend fun deletePlaylist(playlistId: Long)
}