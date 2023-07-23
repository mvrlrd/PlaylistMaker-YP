package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.Song

interface PlaylistInfoRepository {
    fun getPlaylistWithSongs(id: Long): Flow<PlaylistInfo>

   suspend fun removeTrackFromPlaylist(trackId: Long, playlistId: Long): Flow<Int>

    fun getAllSongsForDebug(): Flow<List<Song>>

}