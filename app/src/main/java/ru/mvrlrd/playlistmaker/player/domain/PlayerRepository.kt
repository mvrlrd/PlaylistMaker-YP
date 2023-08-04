package ru.mvrlrd.playlistmaker.player.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.player.data.MyMediaPlayer
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter

interface PlayerRepository {
    fun preparePlayer(playerTrack: PlayerTrack)
    fun pause()
    fun onDestroy()
    fun getCurrentTime(): Flow<Int>

    suspend fun addToFavorite(playerTrack: PlayerTrack)
    suspend fun removeFromFavorite(trackId: Long)
    fun getAllPlaylists(): Flow<List<PlaylistForAdapter>>
    fun getLivePlayerState(): StateFlow<MyMediaPlayer.PlayerState>
    suspend fun addTrackToPlaylist(track: TrackForAdapter, playlist: PlaylistForAdapter): Flow<AddingTrackToPlaylistResult>
    fun getAllPlaylistsWithSongs(): Flow<List<PlaylistForAdapter>>

    fun getFavoriteIds(): Flow<List<Long>>

    fun handleStartAndPause()

    fun stopIt()
}