package ru.mvrlrd.playlistmaker.player.domain.impl

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

    override fun setOnCompletionListener(onComplete: () -> Unit) {
        playerRepository.setOnCompletionListener(onComplete)
    }

    override fun getCurrentTime(): Int {
        return playerRepository.getCurrentTime()
    }

    override fun preparePlayer(playerTrack: PlayerTrack, prepare: () -> Unit) {
        playerRepository.preparePlayer(playerTrack, prepare)
    }

    override suspend fun addTrackToFavorite(playerTrack: PlayerTrack) {
        playerRepository.addToFavorite(playerTrack)
        println("${playerTrack.trackName} was added")
    }

    override suspend fun removeTrackFromFavorite(trackId: Int) {
        playerRepository.removeFromFavorite(trackId)
        println("${trackId} was removed")
    }

}