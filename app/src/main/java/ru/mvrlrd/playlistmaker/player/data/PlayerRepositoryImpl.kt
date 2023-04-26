package ru.mvrlrd.playlistmaker.player.data

import ru.mvrlrd.playlistmaker.player.domain.PlayerRepository

class PlayerRepositoryImpl(private val playerClient: PlayerClient): PlayerRepository {
    //тут пишу логику
    override fun preparePlayer(prepare: () -> Unit) {
        playerClient.preparePlayer(prepare)
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