package ru.mvrlrd.playlistmaker.player.data

import ru.mvrlrd.playlistmaker.player.domain.PlayerRepository
import ru.mvrlrd.playlistmaker.player.domain.TrackForPlayer

class PlayerRepositoryImpl(private val playerClient: PlayerClient): PlayerRepository {
    override fun preparePlayer(trackForPlayer: TrackForPlayer, prepare: () -> Unit) {
        playerClient.preparePlayer(trackForPlayer, prepare)
    }

    override fun setOnCompletionListener(onComplete: () -> Unit) {
        playerClient.setOnCompletionListener(onComplete)
    }

    override fun start() {
        playerClient.start()
    }

    override fun pause() {
        playerClient.pause()
    }

    override fun onDestroy() {
        playerClient.onDestroy()
    }

    override fun getCurrentTime(): Int {
        return playerClient.getCurrentTime()
    }
}