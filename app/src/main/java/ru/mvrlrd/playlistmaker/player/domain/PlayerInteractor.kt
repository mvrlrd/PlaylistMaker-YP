package ru.mvrlrd.playlistmaker.player.domain

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.player.data.MyMediaPlayer
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter

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
    suspend fun addTrackToPlaylist(trackId: TrackForAdapter, playlistId: Long): Flow<Pair<String, Boolean>>
    fun getAllPlaylistsWithQuantities(): Flow<List<PlaylistForAdapter>>
}