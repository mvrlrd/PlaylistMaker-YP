package ru.mvrlrd.playlistmaker.player.domain

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.player.data.MyMediaPlayer

interface PlayerInteractor {
    fun start()
    fun pause()
    fun onDestroy()
    fun getCurrentTime(): Flow<Int>
    fun preparePlayer(playerTrack: PlayerTrack)
    suspend fun addTrackToFavorite(playerTrack: PlayerTrack)
    suspend fun removeTrackFromFavorite(trackId: Long)
    fun getAllPlaylists(): Flow<List<PlaylistForAdapter>>
    fun getLivePlayerState(): LiveData<MyMediaPlayer.PlayerState>
    suspend fun addTrackToPlaylist(trackId: Long, playlistId: Long): Flow<Pair<String, Boolean>>
    fun getAllPlaylistsWithQuantities(): Flow<List<PlaylistForAdapter>>
}