package ru.mvrlrd.playlistmaker.player.domain

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.player.data.MyMediaPlayer
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter

interface PlayerInteractor {
    fun pause()
    fun onDestroy()
    fun getCurrentTime(): Flow<Int>
    fun preparePlayer(playerTrack: PlayerTrack)
    suspend fun addTrackToFavorite(playerTrack: PlayerTrack)
    suspend fun removeTrackFromFavorite(trackId: Long)
    fun getAllPlaylists(): Flow<List<PlaylistForAdapter>>
    fun getLivePlayerState(): Flow<MyMediaPlayer.PlayerState>
    suspend fun addTrackToPlaylist(trackId: TrackForAdapter, playlist: PlaylistForAdapter): Flow<AddingTrackToPlaylistResult>
    fun getAllPlaylistsWithQuantities(): Flow<List<PlaylistForAdapter>>

    fun getFavIds(): Flow<List<Long>>

    fun handleStartAndPause()
    fun stopIt()
}