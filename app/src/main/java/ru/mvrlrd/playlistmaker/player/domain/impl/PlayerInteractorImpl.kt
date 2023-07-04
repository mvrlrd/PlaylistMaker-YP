package ru.mvrlrd.playlistmaker.player.domain.impl

import ru.mvrlrd.playlistmaker.player.domain.PlayerInteractor
import ru.mvrlrd.playlistmaker.player.domain.PlayerRepository
import ru.mvrlrd.playlistmaker.player.domain.TrackForPlayer

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

    override fun setOnCompletionListener(onComplete: () -> Unit) {
        playerRepository.setOnCompletionListener(onComplete)
    }

    override fun getCurrentTime(): Int {
        return playerRepository.getCurrentTime()
    }

    override fun preparePlayer(trackForPlayer: TrackForPlayer, prepare: () -> Unit) {
        playerRepository.preparePlayer(trackForPlayer, prepare)
    }

    override suspend fun addTrackToFavorite(trackForPlayer: TrackForPlayer) {
        playerRepository.addToFavorite(trackForPlayer)
        println("${trackForPlayer.trackName} was added")
    }

    override suspend fun removeTrackFromFavorite(trackId: Int) {
        playerRepository.removeFromFavorite(trackId)
        println("${trackId} was removed")
    }

}