package ru.mvrlrd.playlistmaker.player.domain.impl

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.player.data.MyMediaPlayer
import ru.mvrlrd.playlistmaker.player.domain.AddingTrackToPlaylistResult
import ru.mvrlrd.playlistmaker.player.domain.PlayerInteractor
import ru.mvrlrd.playlistmaker.player.domain.PlayerRepository
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter

class PlayerInteractorImpl(private val playerRepository: PlayerRepository) : PlayerInteractor {

    override fun pause() {
        playerRepository.pause()
    }

    override fun onDestroy() {
        playerRepository.onDestroy()
    }

    override fun getLivePlayerState(): StateFlow<MyMediaPlayer.PlayerState> {
        return playerRepository.getLivePlayerState()
    }

    override fun getCurrentTime(): Flow<Int> {
        return playerRepository.getCurrentTime()
    }

    override fun preparePlayer(playerTrack: PlayerTrack) {
        playerRepository.preparePlayer(playerTrack)
    }

    override suspend fun addTrackToFavorite(playerTrack: PlayerTrack) {
        playerRepository.addToFavorite(playerTrack)
    }

    override suspend fun removeTrackFromFavorite(trackId: Long) {
        playerRepository.removeFromFavorite(trackId)
    }

    override fun getAllPlaylists(): Flow<List<PlaylistForAdapter>> {
        return playerRepository.getAllPlaylists()
    }

    override suspend fun addTrackToPlaylist(
        trackId: TrackForAdapter,
        playlist: PlaylistForAdapter
    ): Flow<AddingTrackToPlaylistResult> {
        return playerRepository.addTrackToPlaylist(trackId, playlist)
    }

    override fun getAllPlaylistsWithQuantities(): Flow<List<PlaylistForAdapter>> {
        return playerRepository.getAllPlaylistsWithSongs()
    }

    override fun getFavIds(): Flow<List<Long>> {
        return playerRepository.getFavoriteIds()
    }

    override fun handleStartAndPause() {
//        for (i in 0 .. 10){
//            Thread.sleep(1000)
//            Log.e("PlayerInteractorImpl", "$i ", )
//        }

        playerRepository.handleStartAndPause()
    }

    override fun stopIt() {
        playerRepository.stopIt()
    }
}