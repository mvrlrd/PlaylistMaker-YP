package ru.mvrlrd.playlistmaker.player.domain.impl

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.player.data.MyMediaPlayer
import ru.mvrlrd.playlistmaker.player.domain.PlayerInteractor
import ru.mvrlrd.playlistmaker.player.domain.PlayerRepository
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack

class PlayerInteractorImpl(private val playerRepository: PlayerRepository): PlayerInteractor {

    override fun start() {
        playerRepository.start()
    }

    override fun pause() {
        playerRepository.pause()
    }

    override fun onDestroy() {
        playerRepository.onDestroy()
    }


   override fun getLivePlayerState():LiveData<MyMediaPlayer.PlayerState>{
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

    override suspend fun removeTrackFromFavorite(trackId: Int) {
        playerRepository.removeFromFavorite(trackId)
    }

    override fun getAllPlaylists(): Flow<List<PlaylistForAdapter>> {
        return playerRepository.getAllPlaylists()
    }
}