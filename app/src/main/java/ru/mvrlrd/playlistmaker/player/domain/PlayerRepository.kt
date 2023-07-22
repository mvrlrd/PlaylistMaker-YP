package ru.mvrlrd.playlistmaker.player.domain

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.player.data.MyMediaPlayer

interface PlayerRepository {
    fun preparePlayer(playerTrack: PlayerTrack)
    fun start()
    fun pause()
    fun onDestroy()
    fun getCurrentTime(): Flow<Int>
    suspend fun addToFavorite(playerTrack: PlayerTrack)
    suspend fun removeFromFavorite(trackId: Long)
    fun getAllPlaylists(): Flow<List<PlaylistForAdapter>>
    fun getLivePlayerState(): LiveData<MyMediaPlayer.PlayerState>
    suspend fun addTrackToPlaylist(trackId: Long, playlistId: Long): Flow<Pair<String, Boolean>>
    fun getAllPlaylistsWithSongs(): Flow<List<PlaylistForAdapter>>
}